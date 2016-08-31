package madballs.gameMode;

import madballs.MadBalls;
import madballs.item.Spawner;
import madballs.player.Player;

/**
 * Created by caval on 31/08/2016.
 */
public class NormalMode extends GameMode {
    private int weaponClassIndex = 0;

    public void setWeaponClassIndex(int weaponClassIndex) {
        this.weaponClassIndex = weaponClassIndex;
    }

    public NormalMode(int weaponClassIndex) {
        super(0);
        this.weaponClassIndex = weaponClassIndex;
    }

    @Override
    public void organize() {
        if (MadBalls.isHost()){
            for (Player player: MadBalls.getMultiplayerHandler().getPlayers()){
                player.getBall().setWeapon(Spawner.getWeapons()[weaponClassIndex], -1);
            }
        }
    }
}
