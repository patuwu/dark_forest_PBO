package dark_forest;
import java.util.Random;
import java.sql.*;

public class Player extends Entity {
    	final Random rnd = new Random();
	private int exp, exp_n;
        private Connection conn;
        private PreparedStatement prep;
        
	public Player(int lv, int ID, Connection conn)
	{
            super(lv, ID, conn);
            this.conn = conn;
            exp = 0;
            exp_n = 150; 
            room = 1;
            grid = 17;
            
            if(ID != 0){
            this.insertData(ID);}
	}
        
        public Player(Connection conn){
            super(1, conn);
            try{
                PreparedStatement prep_r = conn.prepareStatement("SELECT * FROM Player");
                ResultSet retrieve = prep_r.executeQuery();
                
                exp = retrieve.getInt("exp");
                exp_n = retrieve.getInt("exp_n");
                
                
        }catch(SQLException ex){
            System.out.println("entity retrieval error");
        }
        }

	public int getEXP(){
		return exp;
	}

	public int getEXP_N(){
		return exp_n;
	}

	public void setEXP(int exp){
		this.exp = exp;
	}

	public void setEXP_N(int exp_n){
		this.exp_n = exp_n;
	}

	public void addEXP(int amount){
		exp = exp + amount;
		amount = 0;

		if(exp >= exp_n)
		{
			lvl = lvl + 1;
			str = str + 1 + rnd.nextInt(2);
			intel = intel + 1 + rnd.nextInt(2);
			agi = agi + rnd.nextInt(1);
			hp = max_hp = str*100;
			max_mp = mp = intel*20;

			System.out.println("\n--------------------------------");
			System.out.println(name + " LEVELED UP TO LEVEL " + lvl + " !");
			System.out.println("STRENGTH : " + str);
			System.out.println("INTELLIGENCE : " + intel);
			System.out.println("AGILITY : " + agi);
			System.out.println("--------------------------------");

			exp = exp - exp_n;
			Math.max(0, exp);
			amount = exp;
			exp_n = exp_n+100;

			if(amount != 0)
			{addEXP(amount);}
		}
	}

        @Override
        public void updateData(){
            super.updateData();
            
            try{
            PreparedStatement prepp = conn.prepareStatement("UPDATE Player SET exp = (?), exp_n = (?) WHERE Ent_ID = (?)");
            prepp.setInt(1, exp);
            prepp.setInt(2, exp_n);
            prepp.setInt(3, id);
            prepp.executeUpdate(); 
            
            }catch (SQLException ex){
                System.out.println("player saving error");
                ex.printStackTrace();
            }
        }
        
        @Override
        public void insertData(int ID){
            super.insertData(ID);          
            
            try{
            this.prep = conn.prepareStatement("INSERT INTO Player (Ent_ID, exp, exp_n) VALUES (1, ?, ?)");
            this.prep.setInt(1, exp);
            this.prep.setInt(2, exp_n);
            this.prep.executeUpdate(); 
            
            }catch (SQLException ex){
                System.out.println("enemy making error");
                ex.printStackTrace();
            }
        }

        @Override
        public String info(){
            return (exp + " / " + exp_n);
        }
}
