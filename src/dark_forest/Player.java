package dark_forest;
import java.util.Random;

public class Player extends Entity {
    	private Random rnd = new Random();
	private int exp, exp_n, mp, max_mp;
	//public Inventory inv;

	public Player(String nm)
	{
		name = nm;
		exp = 0;
		exp_n = 150;
		str = str + ((lvl-1)+rnd.nextInt(2));
		intel = intel + ((lvl-1)+rnd.nextInt(2));
		agi = agi + ((lvl-1)+rnd.nextInt(2));
		hp = max_hp = str*100;
                hp = hp/2;
		max_mp = mp = intel*20;
	}

	public int getEXP(){
		return exp;
	}

	public int getEXP_N(){
		return exp_n;
	}

	public int getMP(){
		return mp;
	}

	public int getMax_MP(){
		return max_mp;
	}

	public void setEXP(int exp){
		this.exp = exp;
	}

	public void setEXP_N(int exp_n){
		this.exp_n = exp_n;
	}

	public void setMP(int mp){
		this.mp = mp;
	}

	public void setMax_MP(int max_mp){
		this.max_mp = max_mp;
	}

        @Override
	public void info(){
		super.info();
		System.out.println("MP = " + mp + " / " + max_mp);
	}

        @Override
	public void simple_info(){
		super.simple_info();

		System.out.println("MP : " + mp + " / " + max_mp);
		System.out.print("[");
		for(int i = 0; i < max_mp ; i = i + (max_mp/10))
		{
			if (i >= mp) 
			{System.out.print(" ");}
			else
			{System.out.print("|");}
		}
		System.out.print("]\n");

		System.out.println("EXP : " + exp + " / " + exp_n);
		System.out.print("[");
		for(int i = 0; i < exp_n ; i = i + (exp_n/10))
		{
			if (i >= exp) 
			{System.out.print(" ");}
			else
			{System.out.print("-");}
		}
		System.out.print("]\n");
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

	public void reduceMP(int amount){
		mp = mp - amount;
	}

	public void restoreMP(){
		mp = mp + (intel*5);
		if(mp >= max_mp) {mp = max_mp;}
	}
}
