package dark_forest;
import java.util.Random;

public class Enemy extends Entity{
	private Random rnd = new Random();
	private int en_type, trait;

	public Enemy(int lv)
	{
		en_type = rnd.nextInt(4);
		trait = rnd.nextInt(6);
		lvl = lv + rnd.nextInt(2);

		switch(en_type){
			case 0 : str = 7 ; intel = 3 ; agi = 2 ; name = "GOBLIN"; break;
			case 1 : str = 5 ; intel = 4 ; agi = 7 ; name = "BANDIT"; break;
			case 2 : str = 8 ; intel = 5 ; agi = 1 ; name = "DRAGON"; break;
			case 3 : str = 3 ; intel = 12; agi = 4 ; name = "CULTIST"; break;
			case 4 : str = 7 ; intel = 4 ; agi = 10; name = "TRIBESMAN"; break;
		}

		str = str + (lvl);
		intel = intel + (lvl);
		agi = agi + (lvl);
		hp = max_hp = str*100;

		switch(trait){
			case 0 : str = str+10; max_hp = max_hp+max_hp*(15/100); hp = max_hp; 				name = "STRONG " + name; break;
			case 1 : agi = agi+6; 																name = "FAST " + name; break;
			case 2 : intel = intel+10; 															name = name + " SHAMAN"; break;
			case 3 : intel = intel+20; str = str-str*(25/100);									name = "CURSED " + name; break;
			case 4 : max_hp = max_hp+max_hp*(35/100); hp = max_hp; agi = agi - agi*(40/100);	name = "ARMORED " + name; break;
			case 5 : max_hp = max_hp+450; str = str-str*(25/100); hp = max_hp;					name = "MUTATED " + name; break;
			case 6 : agi = agi+17; max_hp = max_hp-max_hp*(25/100); hp = max_hp;				name = "PHANTOM " + name; break;
		}

	}

	public int getEType(){
		return en_type;
	}

	public int getTrait(){
		return trait;
	}

	public void setETypr(int exp){
		this.en_type = en_type;
	}

	public void setTrait(int exp_n){
		this.trait = trait;
	}

	public int GiveExp(){
		return ((str+intel+agi)*(lvl*2));
	}

	public int Attacks(int str, int intel, String ply){
		int rolld = rnd.nextInt(2), damage = 0;

		switch(en_type){
			case 0 :
				switch(rolld){
				case 0 : 
					damage = (this.str-str)*3;
					System.out.println(name + " STABBED " + ply);
					break;

				case 1 : 
					damage = (this.str-str)*2;
					System.out.println(name + " SLASHED " + ply);
					break;

				case 2 : 
					damage = (this.str-str)*1;
					System.out.println(name + " SCRATCHED " + ply);
					break;
				}
			break;

			case 1 :
				switch(rolld){
				case 0 : 
					damage = (this.str-str)*2;
					System.out.println(name + " STABBED " + ply);
					break;

				case 1 : 
					damage = (this.str-str)*4;
					System.out.println(name + " SHOT " + ply);
					break;

				case 2 : 
					damage = (this.str-str)*1;
					System.out.println(name + " SLASHED " + ply);
					break;
				}
			break;

			case 2 :
				switch(rolld){
				case 0 : 
					damage = (this.str-str)*2;
					System.out.println(name + " STABBED " + ply);
					break;

				case 1 : 
					damage = -1;
					System.out.println(name + " HEALS");
					hp = hp + this.intel*10;
					break;

				case 2 : 
					damage = (this.str-str)*5;
					System.out.println(name + " SLAMMED " + ply);
					break;
				}
			break;

			case 3 :
				switch(rolld){
				case 0 : 
					damage = (this.intel-intel)*3;
					System.out.println(name + " CASTED A CURSE TO " + ply);
					break;

				case 1 : 
					damage = -1;
					System.out.println(name + " HEALS");
					hp = hp + this.intel*3;
					break;

				case 2 : 
					damage = (this.intel-intel)*5;
					System.out.println(name + " CASTED A DARK BLAST TO " + ply);
					break;
				}
			break;

			case 4 :
				switch(rolld){
				case 0 : 
					damage = (this.str-str)*3;
					System.out.println(name + " THREW A SPEAR TO " + ply);
					break;

				case 1 : 
					damage = -1;
					System.out.println(name + " HEALS ");
					hp = hp + this.intel*10;
					break;

				case 2 : 
					damage = (this.intel-intel)*5;
					System.out.println(name + " HEXXED " + ply);
					break;
				}
			break;
			}

			if(hp >= max_hp) {hp = max_hp;}
			return damage;
		}

}
