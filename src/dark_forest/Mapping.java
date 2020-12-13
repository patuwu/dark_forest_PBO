package dark_forest;
import java.sql.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

public class Mapping implements Runnable {
    final Random rnd = new Random();
    private Thread t;
    private Connection conn;
    private PreparedStatement prep;
    private long start, end;
    private int[] inserted;
    private boolean boss, shop, trea;
    
    public Mapping(Connection conn){
        this.conn = conn;
    }
    
    @Override
    public void run(){
        try{
            
            ArrayList<Integer> ban = new ArrayList<Integer>();
            ArrayList<Integer> done = new ArrayList<Integer>();
            int j;
            boolean deleted;
            for(int i = 1; i <= 9; i++){
                
                System.out.println(i);
                PreparedStatement prepC = conn.prepareStatement("SELECT * FROM Rooms WHERE room_ID = (?)");
                prepC.setInt(1, i);
                ResultSet check = prepC.executeQuery();
                
                j = 1 + rnd.nextInt(8);                
                if(check.getInt("right_Room") == 0 && ban.contains(j) == false && i != j){
                    updateLink(i,j,"left_Room" ,"right_Room");
                    ban.add(j);
                    j = 1 + rnd.nextInt(8);                        
                }
                
                if(check.getInt("up_Room") == 0 && ban.contains(j) == false && i != j){
                    updateLink(i,j,"bot_Room" ,"up_Room");
                    ban.add(j);
                    j = 1 + rnd.nextInt(8);                                              
                }
                
                if(check.getInt("left_Room") == 0 && ban.contains(j) == false && i != j){
                    updateLink(i,j,"right_Room" ,"left_Room");
                    ban.add(j);
                    j = 1 + rnd.nextInt(8);                       
                }
                
                if(check.getInt("bot_Room") == 0 && ban.contains(j) == false && i != j){
                    updateLink(i,j,"up_Room" ,"bot_Room");
                    ban.add(j);
                    j = 1 + rnd.nextInt(8);
                }
                
                if(i == 9){
                    if(check.getInt("right_Room") == 0){
                    updateLink(9,10,"left_Room" ,"right_Room");
                    }
                    else if(check.getInt("up_Room") == 0){
                    updateLink(9,10,"bot_Room" ,"up_Room");
                    }
                    else if(check.getInt("left_Room") == 0){
                    updateLink(9,10,"right_Room" ,"left_Room");
                    }
                    else if(check.getInt("bot_Room") == 0){
                    updateLink(9,10,"up_Room" ,"bot_Room");
                    }
                }
                
            }
        }catch(Exception e){
            System.out.println("thread error");
            e.printStackTrace();
        }
    }
    
    private void updateLink(int source, int target, String source_dir, String target_dir){
        try{
            PreparedStatement update = conn.prepareStatement("UPDATE Rooms SET "+ target_dir +" = (?) WHERE room_ID = (?)");
            update.setInt(1, target);
            update.setInt(2, source);
            update.executeUpdate();
            update = conn.prepareStatement("UPDATE Rooms SET "+ source_dir +" = (?) WHERE room_ID = (?)");
            update.setInt(1, source);
            update.setInt(2, target);
            update.executeUpdate();
            System.out.println("Updated " + target_dir + " - " + source + " connected to " + target);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public void GenerateRoom(){
        try{
            prep = conn.prepareStatement("INSERT INTO Rooms (room_ID, is_BossRoom, is_ShopRoom, is_TreaRoom) VALUES (?, ?, ?, ?)");
            
            
            for(int i = 1; i <= 9; i++){
            prep.setInt(1, i);
            boss = false;
            trea = false;
            shop = false;
            
            if(i == 9){
            boss = true;}
            
            else if(i == 5){
            shop = true;}
            
            else if(i == 7){
            trea = true;}
            
            prep.setBoolean(2, boss);
            prep.setBoolean(3, shop);
            prep.setBoolean(4, trea);
            prep.addBatch();     
            }
            
            start = System.currentTimeMillis();
            inserted = prep.executeBatch();
            end = System.currentTimeMillis();
            prep.close();
            
            System.out.println("total time taken to insert the batch = " + (end - start) + " ms");
                      
            t = new Thread(this, "map shaping");
            t.start();
            
            prep = conn.prepareStatement("INSERT INTO Grid VALUES (?,?,?)");
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++){                   
                    prep.setInt(1, ((8*i)+j));
                    
                    if(( i < 2 && j < 2 ) || ( i < 2 && j > 5) || ( i > 5 && j < 2 ) || ( i > 5 && j > 5))
                    {prep.setBoolean(2, false);}
                    else
                    {prep.setBoolean(2, true);}
                    
                    
                    if(( i == 0 && (j >= 2 && j <= 5)) || ((i >= 2 && i <= 5) && j == 0) || ( i == 7 && (j >= 2 && j <= 5)) || ( (i >= 2 && i <= 5) && j == 7))
                    {prep.setBoolean(3, true);}
                    else
                    {prep.setBoolean(3, false);}
                    
                    
                    prep.addBatch();
                    }
                }
            start = System.currentTimeMillis();
            inserted = prep.executeBatch();
            end = System.currentTimeMillis();
            prep.close();
            
            System.out.println("generate map = " + (end - start) + " ms");                

            System.out.println("generate finish");
            
            try {
                t.join();
            } catch (InterruptedException ex) {              
            }
        } catch (SQLException ex)
            {
            System.out.println("generating error");
            ex.printStackTrace();
            }
    }
    
    public void updateGrid(int room_id){
        try{            
            PreparedStatement updateGrid = conn.prepareStatement("UPDATE Grid SET is_Passable = true WHERE is_Edge = true");
            updateGrid.executeUpdate();
            
            PreparedStatement prepRoom = conn.prepareStatement("SELECT * FROM Rooms WHERE room_ID = (?)");
            prepRoom.setInt(1, room_id);
            ResultSet RoomCheck = prepRoom.executeQuery();
            
            if(RoomCheck.getInt("left_Room") == 0){
                int[] change = {16,24,32,40};
                updateGrid = conn.prepareStatement("UPDATE Grid SET is_Passable = false WHERE grid = (?)");               
                for(int i = 0; i < change.length; i++){
                updateGrid.setInt(1, change[i]);
                updateGrid.addBatch();
                }
                updateGrid.executeBatch();                
            }
            
            if(RoomCheck.getInt("right_Room") == 0){
                int[] change = {23,31,39,47};
                updateGrid = conn.prepareStatement("UPDATE Grid SET is_Passable = false WHERE grid = (?)");               
                for(int i = 0; i < change.length; i++){
                updateGrid.setInt(1, change[i]);
                updateGrid.addBatch();
                }
                updateGrid.executeBatch();               
            }
            
            if(RoomCheck.getInt("bot_Room") == 0){
                int[] change = {58,59,60,61};
                updateGrid = conn.prepareStatement("UPDATE Grid SET is_Passable = false WHERE grid = (?)");               
                for(int i = 0; i < change.length; i++){
                updateGrid.setInt(1, change[i]);
                updateGrid.addBatch();
                }
                updateGrid.executeBatch();               
            }
            
            if(RoomCheck.getInt("up_Room") == 0){
                int[] change = {2,3,4,5};
                updateGrid = conn.prepareStatement("UPDATE Grid SET is_Passable = false WHERE grid = (?)");               
                for(int i = 0; i < change.length; i++){
                updateGrid.setInt(1, change[i]);
                updateGrid.addBatch();
                }
                updateGrid.executeBatch();                
            }
            
        }catch(SQLException ex){
            
        }
    }
    
    public void EnemyPlacing(){
        try{
            PreparedStatement get = conn.prepareStatement("SELECT * FROM Entity");
            PreparedStatement prep2 = conn.prepareStatement("UPDATE Entity SET grid = (?), room = (?) WHERE Ent_ID = (?)");
            ResultSet lookResult = get.executeQuery();
            
            while(lookResult.next()){
                int[] gridAllowed = {18,20,21,26,27,28,29,34,35,36,37,42,43,44,45};
                int[] roomAllowed = {1,2,3,4,6,8};
                int a = gridAllowed[rnd.nextInt(15)];
                int b = roomAllowed[rnd.nextInt(6)];
                           
                PreparedStatement prepcheck = conn.prepareStatement("SELECT * FROM Entity");
                ResultSet check = prepcheck.executeQuery();
                
                while(check.next()){
                    if((check.getInt("grid") == a && check.getInt("room") == b)){
                        a = gridAllowed[rnd.nextInt(15)];
                        b = roomAllowed[rnd.nextInt(6)];
                        check = get.executeQuery();
                    }                           
                }
                
                if(lookResult.getInt("Ent_ID") == 1){
                    a = 17;
                    b = 1;
                }
                
                prep2.setInt(1, a);
                prep2.setInt(2, b);
                prep2.setInt(3, lookResult.getInt("Ent_ID"));
                prep2.addBatch();
            }
            
            start = System.currentTimeMillis();
            inserted = prep2.executeBatch();
            end = System.currentTimeMillis();
            prep2.close();
            
            System.out.println("total time taken to insert the batch = " + (end - start) + " ms");
            
        }catch (SQLException ex)
        {
        System.out.println("generating error");
        ex.printStackTrace();
        }
    }
}
