package dark_forest;
import java.sql.*;

public class Entity {
    public String name;
    public int lvl, str, intel, agi, hp, max_hp, mp, max_mp, grid, room, id;
    private Connection conn;
    PreparedStatement prep;
    
    public Entity(int lv, int ID, Connection conn){
        this.conn = conn;
        name = "0_null";
        lvl = 1;
        str = intel = agi = 5;
        hp = max_hp = str*100;
        max_mp = mp = intel*20;
        id = ID;
    }

    public Entity(int ID, Connection conn){
        this.conn = conn;
        try{
                this.prep = conn.prepareStatement("SELECT * FROM Entity WHERE Ent_ID = (?)");
                prep.setInt(1, ID);
                ResultSet retrieve = prep.executeQuery();
                
                id = retrieve.getInt("Ent_ID");
                name = retrieve.getString("name");
                lvl = retrieve.getInt("lvl");
                str = retrieve.getInt("str");
                agi = retrieve.getInt("agi");
                intel = retrieve.getInt("intel");
                hp = retrieve.getInt("hp");
                max_hp = retrieve.getInt("max_hp");
                mp = retrieve.getInt("mp");
                max_mp = retrieve.getInt("max_mp");
                grid = retrieve.getInt("grid");
                room = retrieve.getInt("room");
                
        }catch(SQLException ex){
            System.out.println("entity retrieval error");
            ex.printStackTrace();
        }
    }
    
    public String getName(){
        return name;
    }

    public int getID(){
        return id;
    }
    
    public int getLvl(){
        return lvl;
    }

    public int getStr(){
        return str;
    }

    public int getInte(){
        return intel;
    }

    public int getAgi(){
        return agi;
    }

    public int getHP(){
        return hp;
    }

    public int getmax_HP(){
        return max_hp;
    }

    public int getMP(){
        return mp;
    }

    public int getmax_MP(){
        return max_mp;
    }
        
    public void setName(){
        this.name = name;
    }

    public void setLvl(int lvl){
        this.lvl = lvl;
    }

    public void setStr(int str){
        this.str = str;
    }

    public void setInt(int intel){
        this.intel = intel;
    }

    public void setAgi(int agi){
        this.agi = agi;
    }

    public void setHP(int hp){
        this.hp = hp;
    }

    public void setmax_HP(int hp){
        this.max_hp = max_hp;
    }

    public void setMP(int mp){
        this.mp = mp;
    }

    public void setmax_MP(int max_mp){
        this.max_mp = max_mp;
    }        
    
    public int getGrid(){
        return grid;
    }
    
    public void setGrid(int grid){
        this.grid = grid;
    }
    
    public int getRoom(){
        return room;
    }
    
    public void setRoom(int room){
        this.room = room;
    }
        
    public void insertData(int ID){
        try{
            prep = conn.prepareStatement("INSERT INTO Entity VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0)");
            prep.setInt(1, ID);
            prep.setString(2, name);
            prep.setInt(3, str);
            prep.setInt(4, intel);
            prep.setInt(5, agi);
            prep.setInt(6, lvl);
            prep.setInt(7, hp);
            prep.setInt(8, max_hp);
            prep.setInt(9, mp);
            prep.setInt(10, max_mp);
            prep.executeUpdate();
            
           System.out.println("entity saving complete");
        }catch(SQLException ex){
            System.out.println("entity insertion failed");
            ex.printStackTrace();
        }
    }
    
    public void updateData(){
        try{
            prep = conn.prepareStatement("UPDATE Entity SET str = (?), intel = (?), agi = (?), lvl = (?), hp = (?), max_hp = (?), mp = (?), max_mp = (?), grid = (?), room = (?) WHERE Ent_ID = (?)");
            prep.setInt(1, str);
            prep.setInt(2, intel);
            prep.setInt(3, agi);
            prep.setInt(4, lvl);
            prep.setInt(5, hp);
            prep.setInt(6, max_hp);
            prep.setInt(7, mp);
            prep.setInt(8, max_mp);
            prep.setInt(9, grid);
            prep.setInt(10, room);
            prep.setInt(11, id);
            prep.executeUpdate(); 
            
            System.out.println("entity saving complete");
            
            }catch (SQLException ex){
                System.out.println("entity saving error");
                ex.printStackTrace();
            }
    }
    
    public void info(){
        System.out.println("NAME = " + name);
        System.out.println("LEVEL = " + lvl);
        System.out.println("STRENGTH = " + str);
        System.out.println("INTELLIGENCE = " + intel);
        System.out.println("AGILITY = " + agi);
        System.out.println("HP = " + hp + " / " + max_hp);
    }

    public void Damage(int amount){
        hp = hp - amount;
    }

    public void Heal(int amount){
        hp = hp + amount;
        if(hp > max_hp)
        {
            hp = max_hp;
        }
    }
        
    public void reduceMP(int amount){
        mp = mp - amount;
    }

    public void restoreMP(){
        mp = mp + (intel*5);
        if(mp >= max_mp) {mp = max_mp;}
    }

}
