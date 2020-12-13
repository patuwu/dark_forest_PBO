package dark_forest;
import java.util.Random;
import java.sql.*;

public class Menu implements actions{
	 Random rng = new Random();
         public Connection conn;
         private int dmg, dmg_multi, str, inte, def, agi, str_ign, int_ign, mp_cost;
         private int dodge_success;
         private PreparedStatement prep;
         
         @Override
	 public void attack(Entity host, Entity target, int atk, Connection conn)
	 {
            dodge_success = 0;
            this.conn = conn;
            
            if(atk == 17){
                if(host.getMP() >= 50){
                host.reduceMP(50);
                dmg = host.getInte()*25;
                host.Heal(dmg);
                System.out.println(host.getName() + " HEALED FOR " + dmg + " !");
                }
                
                else{
                    System.out.println(host.getName() + " IS OUT OF MANA !");
                }
            }
            else{
            try{
                prep = conn.prepareStatement("SELECT * FROM Attacks WHERE Atk_ID is (?)");
		prep.setInt(1, atk);
		ResultSet stat = prep.executeQuery();
                
                dmg_multi = stat.getInt("dmg_multi");
                str = stat.getInt("str_multi");
                inte = stat.getInt("int_multi");
                def = stat.getInt("def_multi");
                agi = stat.getInt("agi_cond");
                str_ign = stat.getInt("str_ignore");
                int_ign = stat.getInt("int_ignore");
                mp_cost = stat.getInt("mp_cost");
                
                if(host.getMP() >= mp_cost){
                    host.reduceMP(mp_cost);
                    dmg = ( ((host.getStr()*str) + (host.getInte()*inte)) - ( ( ( (target.getStr()-((target.getStr()*str_ign)/100)) + (target.getInte()-((target.getInte()*int_ign)/100) ) )  /def ) ) ) * dmg_multi;
                    dmg = Math.max(0, dmg);
                    
                if((rng.nextInt(host.getAgi()) + host.getAgi() + agi) < rng.nextInt(target.getAgi())) {dodge_success = 1;}
                    
                if(dodge_success == 1)
                    {System.out.println(host.getName() + " MISSES !");}
                else
                    {
                        System.out.println(target.getName() + " RECIEVE " + dmg + " DAMAGE !");
                        target.Damage(dmg);
                    }                   
                }
                else{
                    System.out.println(host.getName() + " IS OUT OF MANA !");
                }
                
                
            } catch (SQLException ex)
                {System.out.println("error");}
            
            }
	 }
}
