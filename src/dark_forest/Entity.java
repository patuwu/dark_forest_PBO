package dark_forest;

public class Entity {
	public String name;
	public int lvl, str, intel, agi, hp, max_hp;

	public Entity()
	{
		name = "0_null";
		lvl = 1;
		str = intel = agi = 5;
		hp = max_hp = str*100;
	}

	public String getName(){
		return name;
	}

	public int getLvl(){
		return lvl;
	}

	public int getStr(){
		return str;
	}

	public int getInt(){
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

	public void info(){
		System.out.println("NAME = " + name);
		System.out.println("LEVEL = " + lvl);
		System.out.println("STRENGTH = " + str);
		System.out.println("INTELLIGENCE = " + intel);
		System.out.println("AGILITY = " + agi);
		System.out.println("HP = " + hp + " / " + max_hp);
	}

	public void simple_info(){
		System.out.println(name + " LVL " + lvl);
		System.out.println("HP : " + hp + " / " + max_hp);
		System.out.print("[");
		for(int i = 0; i < max_hp ; i = i + (max_hp/10))
		{
			if (i >= hp) 
			{System.out.print(" ");}
			else
			{System.out.print("|");}
		}
		System.out.print("]\n");
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
}
