package dark_forest;

public interface actions {
    void attack(Entity host, Entity target, int dmg_multi, int def_multi, int agi_cond);
    void spell(Entity host, Entity target, int dmg_multi, int def_multi, int agi_cond);
}
