package class02;


import org.junit.Test;

public class TowerTests {

    @Test
    public void test1() {
        System.out.println();
        SharpTower.drawTower(5);
    }

    @Test
    public void test2() {
        System.out.println();
        SharpTower.drawTower(0);

    }

    @Test
    public void test3() {
        System.out.println();
        SharpTower.drawTower(1);
    }
}
