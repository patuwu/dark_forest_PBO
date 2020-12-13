package dark_forest;
import java.util.Random;
import java.sql.*;
import java.util.Arrays;

public class Enemy extends Entity{
	final Random rnd = new Random();
	private int en_type, trait;
        private int[] atk_list;
        private String list;
        private Connection conn;
        private PreparedStatement prep;

	public Enemy(int lv, int ID, Connection conn){
            super(lv, ID, conn);
            this.conn = conn;
            
            en_type = rnd.nextInt(4);
            trait = rnd.nextInt(7);
//            lvl = lv + rnd.nextInt(2);

            switch(en_type){
		case 0 : str = 7 ; intel = 3 ; agi = 2 ; atk_list = new int[] {1,4,5,6,15}; name = "GOBLIN"; break;
		case 1 : str = 5 ; intel = 4 ; agi = 7 ; atk_list = new int[] {3,4,6,8}; name = "BANDIT"; break;
		case 2 : str = 8 ; intel = 5 ; agi = 1 ; atk_list = new int[] {5,7,9,13,17}; name = "DRAGON"; break;
		case 3 : str = 3 ; intel = 12; agi = 4 ; atk_list = new int[] {6,9,12,13,17}; name = "CULTIST"; break;
		case 4 : str = 7 ; intel = 4 ; agi = 10; atk_list = new int[] {3,4,14,16,17}; name = "TRIBESMAN"; break;
            }

            list = Arrays.toString(atk_list);           
            str = str + (lvl);
            intel = intel + (lvl);
            agi = agi + (lvl);
            hp = max_hp = str*100;

            switch(trait){
                case 0 : break;
		case 1 : str = str+10; max_hp = max_hp+max_hp*(15/100); hp = max_hp; 			name = "STRONG " + name; break;
		case 2 : agi = agi+6; 									name = "FAST " + name; break;
		case 3 : intel = intel+10; 								name = name + " SHAMAN"; break;
		case 4 : intel = intel+20; str = str-str*(25/100);					name = "CURSED " + name; break;
		case 5 : max_hp = max_hp+max_hp*(35/100); hp = max_hp; agi = agi - agi*(40/100);	name = "ARMORED " + name; break;
		case 6 : max_hp = max_hp+450; str = str-str*(25/100); hp = max_hp;			name = "MUTATED " + name; break;
		case 7 : agi = agi+17; max_hp = max_hp-max_hp*(25/100); hp = max_hp;			name = "PHANTOM " + name; break;
            }
            
            if(ID != 0){
            this.insertData(ID);}
	}
        
        public Enemy(int ID, Connection conn){
            super(ID, conn);
            try{
                PreparedStatement prep_e = conn.prepareStatement("SELECT * FROM Enemy WHERE Ent_ID = (?)");
                prep_e.setInt(1, ID);
                ResultSet retrieve = prep_e.executeQuery();
                
                en_type = retrieve.getInt("En_type");
                trait = retrieve.getInt("trait");
                String lists = retrieve.getString("atk_list");
                String [] atk = lists.replace("[", "").replace("]", "").split(", ");
                atk_list = new int[atk.length];
                
                for(int i = 0; i < atk.length; i++){
                    atk_list[i] = Integer.parseInt(atk[i]);
                }
                              
        }catch(SQLException ex){
            System.out.println("entity retrieval error");
            ex.printStackTrace();
        }
        }

	public int getEType(){
            return en_type;
	}

	public int getTrait(){
            return trait;
	}

	public int[] getAtkList(){
            return atk_list;
	}        

	public int GiveReward(){
            return ((str+intel+agi)*(lvl*2));
	}

	public int RollAttack(){
            int use = rnd.nextInt(atk_list.length);
            return atk_list[use];
	}
        
        @Override
        public void insertData(int ID){
            super.insertData(ID);
            
            try{
            this.prep = conn.prepareStatement("INSERT INTO Enemy VALUES (?, ?, ?, ?)");
            this.prep.setInt(1, ID);
            this.prep.setInt(2, en_type);
            this.prep.setInt(3, trait);
            this.prep.setString(4, list);
            this.prep.executeUpdate();  
            
            System.out.println("enemy saving complete");
            
            }catch (SQLException ex){
                System.out.println("enemy making error");
                ex.printStackTrace();
            }
        }
        
}
