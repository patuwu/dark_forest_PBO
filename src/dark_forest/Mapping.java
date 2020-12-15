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
    
    public Mapping(Connection conn){
        this.conn = conn;
    }
    
    @Override
    public void run(){
        try{
            
            int[][] mapping =   {
                                {0,0,0,0,0},
                                {0,0,0,0,0},
                                {0,0,0,0,0},
                                {0,0,0,0,0},                                
                                {0,0,0,0,0}
                                };
            ArrayList<String> ban = new ArrayList<>();
            int i = 0;
            int hz = 2;
            int vr = 2;
            int dir = rnd.nextInt(3);
            mapping[hz][vr] = i;
            i++;
            
            while(i <= 10){                    
                    switch(dir){
                        case 0 :
                            if((hz-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz-1,vr}))){
                                hz--;
                            }
                            else if((vr-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz,vr-1}))){
                                vr--;
                            }
                            else if((hz+1) <= 4 && !ban.contains(Arrays.toString(new int[] {hz+1,vr}))){
                                hz++;
                            }
                            else if((vr++) <= 4 && !ban.contains(Arrays.toString(new int[] {hz,vr+1}))){
                                vr++;
                            }
                            break;
                        case 1 :
                            if((vr-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz,vr-1}))){
                                vr--;
                            }
                            else if((hz-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz-1,vr}))){
                                hz--;
                            }
                            else if((hz+1) <= 4 && !ban.contains(Arrays.toString(new int[] {hz+1,vr}))){
                                hz++;
                            }
                            else if((vr+1) <= 4 && !ban.contains(Arrays.toString(new int[] {hz,vr+1}))){
                                vr++;
                            }
                            break;
                        case 2 :
                            if((hz+1) <= 4 && !ban.contains(Arrays.toString(new int[] {hz+1,vr}))){
                                hz++;
                            }
                            else if((hz-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz-1,vr}))){
                                hz--;
                            }
                            else if((vr-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz,vr-1}))){
                                vr--;
                            }
                            else if((vr++) <= 4 && !ban.contains(Arrays.toString(new int[] {hz,vr +1}))){
                                vr++;
                            }
                            break;
                        case 3 :
                            if((vr++) <= 4 && !ban.contains(Arrays.toString(new int[] {hz,vr+1}))){
                                vr++;
                            }
                            else if((hz+1) <= 4 && !ban.contains(Arrays.toString(new int[] {hz+1,vr}))){
                                hz++;
                            }
                            else if((vr-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz,vr-1}))){
                                vr--;
                            }
                            else if((hz-1) >= 0 && !ban.contains(Arrays.toString(new int[] {hz-1,vr}))){
                                hz--;
                            }
                            break;
                    }
                    dir = rnd.nextInt(3);
                    if(hz == -1 || hz == 5 | vr == -1 || vr == 5 || ban.contains(Arrays.toString(new int[] {hz,vr}))){
                        hz = rnd.nextInt(4);
                        vr = rnd.nextInt(4);
                    }
                    else{               
                    mapping[hz][vr] = i;
                    ban.add(Arrays.toString(new int[] {hz,vr}));
                    i++;}
                }
                System.out.println(Arrays.deepToString(mapping).replace("], ", "]\n"));
                
                PreparedStatement prepInsert = conn.prepareStatement("UPDATE Rooms SET left_Room = (?), right_Room = (?), up_Room = (?), bot_Room = (?) WHERE room_ID = (?) ");
                
                for(i = 0; i <= 4; i++){
                    for(int j = 0; j <= 4; j++){
                        if(mapping[i][j] != 0){
                            if(j > 0){
                                prepInsert.setInt(1, mapping[i][j-1]);
                            }else{prepInsert.setInt(1, 0);}
                            if(j < 4){
                                prepInsert.setInt(2, mapping[i][j+1]);
                            }else{prepInsert.setInt(2, 0);}                            
                            if(i > 0){
                                prepInsert.setInt(3, mapping[i-1][j]);
                            }else{prepInsert.setInt(3, 0);}
                            if(i < 4){
                                prepInsert.setInt(4, mapping[i+1][j]);
                            }else{prepInsert.setInt(4, 0);}
                            prepInsert.setInt(5, mapping[i][j]);
                            prepInsert.executeUpdate();
                        }
                    }
                }
            
        }catch(Exception e){
            System.out.println("thread error");
            e.printStackTrace();
        }
    }
       
    public void GenerateRoom(){
        try{
            prep = conn.prepareStatement("INSERT INTO Rooms (room_ID) VALUES (?)");
            
            
            for(int i = 1; i <= 9; i++){
            prep.setInt(1, i);                                             
            prep.addBatch();     
            }
            
            start = System.currentTimeMillis();
            inserted = prep.executeBatch();
            end = System.currentTimeMillis();
            prep.close();
            
            System.out.println("room insert = " + (end - start) + " ms");
                      
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
                int[] roomAllowed = {1,2,3,4,5,6,7,8};
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
            
            System.out.println("enemy placing = " + (end - start) + " ms");
            
        }catch (SQLException ex)
        {
        System.out.println("generating error");
        ex.printStackTrace();
        }
    }
}
