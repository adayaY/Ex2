import static org.junit.jupiter.api.Assertions.*;

class Test {

    @org.junit.jupiter.api.Test
    void getOrder() {
    }

    @org.junit.jupiter.api.Test
    void testToString() {
    }

    @org.junit.jupiter.api.Test
    void setData() {
    }

    @org.junit.jupiter.api.Test
    void getData() {
    }

    @org.junit.jupiter.api.Test
    void getType() {
    }

    @org.junit.jupiter.api.Test
    void setType() {
    }

    @org.junit.jupiter.api.Test
    void setOrder() {
    }
    // test Scell
    @org.junit.jupiter.api.Test
    void isNumber() {
        assertTrue(SCell.isNumber("12345"));
        assertTrue(SCell.isNumber("123.45"));
        assertTrue(SCell.isNumber("-123.45"));
        assertTrue(SCell.isNumber("0"));
        assertTrue(SCell.isNumber("9876543210"));
        assertFalse(SCell.isNumber(""));
        assertFalse(SCell.isNumber(null));
        assertFalse(SCell.isNumber("123!"));
        assertFalse(SCell.isNumber("12 34"));
        assertFalse(SCell.isNumber("123.45.67"));
    }

    @org.junit.jupiter.api.Test
    void isTexst() {
        assertTrue(SCell.isText("hello"));
        assertTrue(SCell.isText("שלום"));
        assertTrue(SCell.isText("abcXYZ"));
        assertFalse(SCell.isText("hello123"));
        assertFalse(SCell.isText("12345"));
        assertFalse(SCell.isText("hello!"));
        assertFalse(SCell.isText("hello world"));
        assertFalse(SCell.isText(""));
        assertFalse(SCell.isText(null));
    }

    @org.junit.jupiter.api.Test
    void isFrom() {
        assertTrue(SCell.isForm("=A1+B2"));
        assertTrue(SCell.isForm("=5+5"));
        assertTrue(SCell.isForm("=A1+(B2*C3)"));
        assertTrue(SCell.isForm("=F5"));
        assertFalse(SCell.isForm("A+B"));
        assertFalse(SCell.isForm("=("));
        assertFalse(SCell.isForm("=A1+B2!"));
        assertFalse(SCell.isForm("=123+"));
        assertFalse(SCell.isForm("=5++5"));
        assertFalse(SCell.isForm(""));
        assertFalse(SCell.isForm(null));
    }

    @org.junit.jupiter.api.Test
    void ifOpTwins(){
        assertTrue(SCell.ifOpTwins("5+3-4"));
        assertTrue(SCell.ifOpTwins("12345"));
        assertTrue(SCell.ifOpTwins("5+3"));
        assertTrue(SCell.ifOpTwins("5-3"));
        assertFalse(SCell.ifOpTwins("5**3"));
        assertFalse(SCell.ifOpTwins("5*-/4"));
        assertFalse(SCell.ifOpTwins("++5+3"));
    }

    @org.junit.jupiter.api.Test
    void opValue(){
        double[] result1 = SCell.opValue("5+3*2-1");
        assertNotNull(result1);
        assertEquals(0, result1[0]);
        assertEquals(2, result1[1]);
        assertEquals(1, result1[3]);
        assertEquals(2, result1[5]);
        assertEquals(0, result1[6]);

        double[] result2 = SCell.opValue("10+3*7-8/2");
        assertNotNull(result2);
        assertEquals(2, result2[2]);  // סימן '+' אמור להחזיר 2
        assertEquals(1, result2[4]);  // סימן '*' אמור להחזיר 1
        assertEquals(2, result2[6]);  // סימן '-' אמור להחזיר 2
        assertEquals(1, result2[8]);  // סימן '/' אמור להחזיר 1

    }

    @org.junit.jupiter.api.Test
    void helpComputeForm() {

        double result1 = SCell.helpComputeForm("=5+3");
        assertEquals(8.0, result1);
        double result2 = SCell.helpComputeForm("5+3");
        assertEquals(8.0, result2);
        double result3 = SCell.helpComputeForm("=(5+3)*(6-4)+2");
        assertEquals(18.0, result3);
        double result4 = SCell.helpComputeForm("=0");
        assertEquals(0.0, result4);
        double result5 = SCell.helpComputeForm("=5+5+5+5");
        assertEquals(20.0, result5);
    }
@org.junit.jupiter.api.Test
    void computeForm(){
    assertEquals(70.0, SCell.computeForm("(6+4)*(8-1)"));
    assertEquals(0.0, SCell.computeForm("5-5"));
    assertEquals(10.0, SCell.computeForm("(5+5)*(2-1)"));
    assertEquals(9.0, SCell.computeForm("5+3+1"));
    assertEquals(0.0, SCell.computeForm("5*0"));
    assertEquals(100.0, SCell.computeForm("10*10"));

    }

    // test CellEntry
   @org.junit.jupiter.api.Test
   void testConstructor() {
       CellEntry cell1 = new CellEntry("");
       assertEquals("", cell1.toString());

       CellEntry cell2 = new CellEntry("=A1");
       assertEquals("A1", cell2.toString());

       CellEntry cell3 = new CellEntry("B1");
       assertEquals("B1", cell3.toString());

       CellEntry cell4 = new CellEntry("=AB100");
       assertEquals("AB100", cell4.toString());
   }

    @org.junit.jupiter.api.Test
    void testIsValid() {
        CellEntry Test1 = new CellEntry("A1");
        assertTrue(Test1.isValid()); // תא חוקי

        CellEntry Test2 = new CellEntry("A100");
        assertFalse(Test2.isValid()); // מספר גדול מ-99

        CellEntry Test3 = new CellEntry("Z0");
        assertTrue(Test3.isValid()); //

        CellEntry Test4 = new CellEntry("AB1");
        assertFalse(Test4.isValid());

        CellEntry Test5 = new CellEntry("a1");
        assertTrue(Test5.isValid()); // אות קטנה במקום גדולה
    }

    // טסט לפונקציה getX
    @org.junit.jupiter.api.Test
     void testGetX() {
        CellEntry Test1 = new CellEntry("A1");
        assertEquals(0,Test1.getX()); // A = 0

        CellEntry Test2= new CellEntry("B1");
        assertEquals(1, Test2.getX()); // B = 1

        CellEntry Test3 = new CellEntry("AB1");
        assertEquals(Ex2Utils.ERR, Test3.getX()); //

    }

    // טסט לפונקציה getY
    @org.junit.jupiter.api.Test
    void testGetY() {
        CellEntry Test1 = new CellEntry("A1");
        assertEquals(1, Test1.getY()); // 1

        CellEntry Test2= new CellEntry("B99");
        assertEquals(99, Test2.getY()); // 99

        CellEntry Test3 = new CellEntry("B100");
        assertEquals(Ex2Utils.ERR, Test3.getY()); // מספר מחוץ לטווח החוקי

        CellEntry Test4 = new CellEntry("B-1");
        assertEquals(Ex2Utils.ERR, Test4.getY()); // מספר שלילי
    }

    // test Ex2Sheet
    @org.junit.jupiter.api.Test
    void BasicCellValues() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        sheet.set(0, 0, "123");
        sheet.set(1, 1, "456");
        sheet.set(2, 2, "Hello");

        // Vérification des valeurs
        assertEquals("123", sheet.value(0, 0));
        assertEquals("456", sheet.value(1, 1));
        assertEquals("Hello", sheet.value(2, 2));
    }

    @org.junit.jupiter.api.Test
    void Formulas() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);

        sheet.set(0, 1, "=123+456");
        assertEquals("579.0", sheet.value(0, 1));


        sheet.set(0, 0, "5");
        sheet.set(1, 0, "7");
        sheet.set(1, 2, "=A0+B0");
        assertEquals("12.0", sheet.value(1, 2));
    }

    @org.junit.jupiter.api.Test
    void DepthCalculation() {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);


        sheet.set(0, 0, "5");
        sheet.set(1, 0, "=A0+1");
        sheet.set(2, 0, "=B0+2");

        int[][] depth = sheet.depth();
        assertEquals(0, depth[0][0]);
        assertEquals(1, depth[1][0]);
        assertEquals(2, depth[2][0]);
    }

    @org.junit.jupiter.api.Test
    void SaveAndLoad() throws Exception {
        Ex2Sheet sheet = new Ex2Sheet(3, 3);


        sheet.set(0, 0, "123");
        sheet.set(1, 1, "=A0*2");
        sheet.set(2, 2, "Hello");
        sheet.save("test.csv");
    }

    @org.junit.jupiter.api.Test
    void max() {
        int[][] matrix1 = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        int result1 = Ex2Sheet.max(matrix1);
        assertEquals(9, result1); // הערך המקסימלי במטריצה הוא 9

        int[][] matrix2 = {
                {-1, -2, -3},
                {-4, -5, -6},
                {-7, -8, -9}
        };
        int result2 = Ex2Sheet.max(matrix2);
        assertEquals(-1, result2); // הערך המקסימלי במטריצה הוא -1
}
    }





