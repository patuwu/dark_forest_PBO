package dark_forest;
import java.util.Random;

public class Menu implements actions{
	 Random rng = new Random();

         @Override
	 public void attack(Entity host, Entity target, int dmg_multi, int def_multi, int agi_cond)
	 {
	 	int dodge_cond = 0;
		int damage = (host.getStr()-(target.getStr()/def_multi)) * dmg_multi;
		Math.max(0, damage);
		if((rng.nextInt(host.getAgi()) + host.getAgi() + agi_cond) < rng.nextInt(target.getAgi())) {dodge_cond = 1;}

		if(dodge_cond == 1)
			{System.out.println(host.getName() + " MISSES !");}
		else
		{
			System.out.println(target.getName() + " RECIEVE " + damage + " DAMAGE !");
			target.Damage(damage);
		}
	 }

         @Override
	 public void spell(Entity host, Entity target, int dmg_multi, int def_multi, int agi_cond)
	 {
	 	int dodge_cond = 0;
		int damage = (host.getInt()-target.getInt()/def_multi) * dmg_multi;
		Math.max(0, damage);

		if((rng.nextInt(host.getAgi()) + host.getAgi() + agi_cond) < rng.nextInt(target.getAgi())) {dodge_cond = 1;}

		if(dodge_cond == 1)
		{System.out.println(host.getName() + " MISSES !");}
		else
		{
			System.out.println(target.getName() + " RECIEVE " + damage + " DAMAGE !");
			target.Damage(damage);
		}
	 }

}
