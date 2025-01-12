
import java.io.IOException;

public class Ex2Sheet implements Sheet {
    private Cell[][] table;

    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for (int i = 0; i < x; i = i + 1) {
            for (int j = 0; j < y; j = j + 1) {
                table[i][j] = new SCell("");

            }
        }
        eval();
    }

    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    //הפונקציה מחזירה את הערך של תא שנמצא במיקום מסויים
    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        eval();// מעדכן את הערכים
        Cell c = get(x, y); // יוצר עצם מסוג CELL
        Cell ch = new SCell(c.getData());
        c = ch;
        eval();

        if (c.getType() == -1)
            return "ERR_CYCLE_FORM";

        else if (c.getType() == -2) {
            return "ERR_FORM_FORMAT";
        } else if (c != null) {
            if (c.getType() == Ex2Utils.FORM) {
                ans = eval(x, y); //מחשב את הערך המוצג
            } else {
                ans = c.getData();
            }
        }
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        CellEntry c = new CellEntry(cords);

        Cell ans = null;
        ans = get(c.getX(), c.getY());
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }

    @Override
    public int height() {
        return table[0].length;
    }

    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s);
        table[x][y] = c;

    }

    // בדיקת עומקוקריאה לפונגציה שמחשבת
    @Override
    public void eval() {
        int[][] dd = depth();
        int max = max(dd); //בודק מה העומק הכי גדול
        for (int y = 0; y < max; y++) {
            for (int i = 0; i < this.width(); i++) {
                for (int j = 0; j < this.height(); j++) {
                    Cell c = get(i, j);
                    {
                        String s = eval(i, j);
                    }
                }
            }
        }
    }

    //בודקת אם הקואורדינטות נמצאות בטווח חוקי או לא.
    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx >= 0 && yy >= 0;
        if (xx > 26 && yy > 99)
            ans = false;
        return ans;
    }


    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here
        for (int i = 0; i < this.width(); i++) {
            for (int j = 0; j < this.height(); j++) {
                ans[i][j] = whatTheDepth(get(i, j), i, j);//פעולה שמחזירה מה העומק של איבר

                this.table[i][j].setOrder(ans[i][j]); // מעדכן את העומק בORDER של העצם
            }
        }
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
    }

    @Override
    public void save(String fileName) throws IOException {
    }

    //הפונקציה מחשבת את הערך של תא במיקום מסויים תוך שימוש עזר בפונגציות אחרות
    @Override
    public String eval(int x, int y) {
        String cellData = this.table[x][y].getData();
        String result = "";
        char[] characters = cellData.toCharArray();

        // תיקון נוסחה אם יש סימן מינוס אחרי האות הראשונה
        if (characters.length >= 2 && characters[1] == '-') {
            cellData = "=" + cellData.substring(2);
        }

        // בדיקה אם התא מכיל נוסחה
        if (SCell.isForm(cellData)) {
            get(x, y).setType(3); // עדכון סוג התא לנוסחה
            String[] references = {show(x, y)};
            if (!exist(references, get(x, y))) {
                try {
                    String formula = "=" + whatTheForm(x, y);
                    double computedValue = SCell.helpComputeForm(formula);
                    String computedValueStr = String.valueOf(computedValue);

                    if (SCell.isNumber(computedValueStr)) {
                        new CellEntry(cellData);
                        get(x, y).setType(Ex2Utils.FORM); // סימון התא כנוסחה
                    }

                    result = String.valueOf(computedValue);
                } catch (Exception e) {
                    // טיפול בשגיאות חישוב
                }
            } else {
                this.table[x][y].setType(-1); // תא שגורם ללולאת חישוב
            }
        }

        // בדיקות שגיאה
        if (this.table[x][y].getType() == -1) {
            result += "ERR_CYCLE_FORM"; // שגיאה של תלות מעגלית
        } else if (this.table[x][y].getType() == -2) {
            result += "ERR_FORM_FORMAT"; // שגיאה בפורמט הנוסחה
        } else if (SCell.isNumber(cellData)) {
            result += cellData; // אם זה מספר
        } else if (SCell.isText(cellData)) {
            result += cellData; // אם זה טקסט
        }

        return result;
    }

    //פונגציות עזר..

    //הפונקציה יוצרת מחרוזת שמתארת את מיקום התא על פי קואורדינטות
    public static String show(int x, int y) {
        String[] abc = Ex2Utils.ABC;
        String str = "";
        for (int i = 0; i < abc.length; i++) {
            if (x == i)
                str = abc[i];

        }
        return str + y;
    }

    //הפונקציה בודקת אם קיימת תלות מעגלית או האם יש חזרה בתאים
        public boolean exist(String[] s,Cell cell){
            boolean ans=false;
            char[]arr=cell.getData().toCharArray();
            String str="";
            for(int i=0;i<arr.length;i++){
                if(arr[i]>='A'&& arr[i]<='Z'){
                    String R=arr[i]+"";
                    str =R+arr[i+1];
                    if (i > arr.length - 2) {

                        if(SCell.isNumber(arr[i+2]+""))
                            str+=arr[i+2];
                    }
                    for(int j=0;j<s.length;j++){
                        char [] Char=s[j].toCharArray();

                        if(arr[i]==Char[0] && arr[i+1]==Char[1]){
                            if(Char.length==3 && i<=arr.length-2){
                                if(arr[i+2]==Char[2])
                                    ans=true;
                            }
                            else{
                                ans=true;
                            }
                        }
                    }
                    if(ans) return true;
                    else {
                        CellEntry w=new CellEntry(str);
                        String[]s1=new String[s.length+1];
                        for(int h=0;h<s1.length;h++){
                            if(h==s.length)
                                s1[s.length]=str;
                            else {
                                s1[h]=s[h];}
                        }
                        return exist(s1,get(w.getX(),w.getY()));
                    }
                }
            }
            return false;
        }

        //פונגציה שמוצאת את המקסימום
    public static int max(int[][] s) {
        int max = -2;
        for (int i = 0; i < s.length; i++) {
            for (int j = 0; j < s[i].length; j++) {
                if (max < s[i][j])
                    max = s[i][j];
            }
        }
        return max;
    }

    //מחזיר את הפונקציה ללא משתנים בלי =
    //אם הוא מעגלי זה לא יעבוד
    public String whatTheForm(int x, int y) {
            String data = this.table[x][y].getData();
            if (data.isEmpty()) {
                return "Error";
            }

            // טיפול במקרים של טקסט או שגיאות
            int t = get(x, y).getType();
            if (t == Ex2Utils.TEXT || t == Ex2Utils.ERR_FORM_FORMAT || t == Ex2Utils.ERR_CYCLE_FORM) {
                return "Error";
            }

            String ref = "", res = "";

            // אם התא מכיל מספר, החזר אותו ועדכן את סוג התא
            if (SCell.isNumber(data)) {
                this.table[x][y].setType(Ex2Utils.NUMBER);
                return data;
            }
            char[] chars = data.toCharArray();

            for (int i = 1; i < chars.length; i++) {
                if (i < chars.length - 1) {
                    // זיהוי הפניה לתא אחר
                    if (chars[i] >= 'A' && chars[i] <= 'Z') {
                        ref += chars[i];
                        ref += chars[i + 1];

                        // בדיקה אם קיימת ספרה נוספת
                        if (i > chars.length - 2) {
                            if (SCell.isNumber(String.valueOf(chars[i + 2]))) {
                                int n = Integer.parseInt(String.valueOf(chars[i + 2]));
                                if (n >= 0 && n <= 99) {
                                    ref += chars[i + 2];
                                    i++;
                                }
                            }
                        }
                        i += 2;
                        CellEntry ce = new CellEntry(ref);
                        ref = "";

                        // קריאה רקורסיבית להערכה של התא
                        res += eval(ce.getX(), ce.getY());
                    }
                }
                try {
                    // הוספת התווים הבאים למחרוזת הנוסחה
                    res += chars[i];
                } catch (Exception e) {
                }
            }
            // בדיקת תקינות הנוסחה
            String formula = "=" + res;
            if (!SCell.isForm(formula)) {
                this.table[x][y].setType(-2);
                res = "";
            }
            return res;
        }
// פונגציה שבודקת את העומק
    public int whatTheDepth(Cell cell, int row, int col) {
            int x = row, y = col;
            String currentCell = Ex2Sheet.show(x, y);
            String[] visitedCells = {currentCell};

            // בדיקה אם התוכן בתא הוא נוסחה
            if (SCell.isForm(get(x, y).getData())) {

                // בדיקה אם התא יוצר לולאה
                if (!exist(visitedCells, cell)) {
                    if (!cell.getData().isEmpty()) {
                        String data = cell.getData();
                        char[] chars = data.toCharArray();
                        String reference = "";

                        // חישוב עומק המבוסס על הפניות לתאים אחרים
                        for (int i = 0; i < chars.length - 1; i++) {
                            if (chars[i] >= 'A' && chars[i] <= 'Z') {
                                reference += chars[i];
                                reference += chars[i + 1];

                                if (i > chars.length - 2) {
                                    if (SCell.isNumber(String.valueOf(chars[i + 2]))) {
                                        int num = Integer.parseInt(String.valueOf(chars[i + 2]));
                                        if (num >= 0 && num <= 99) {
                                            reference += chars[i + 2];
                                        }
                                    }
                                }

                                // חישוב עומק עבור התא המופנה
                                CellEntry referencedCell = new CellEntry(reference);
                                return whatTheDepth(table[referencedCell.getX()][referencedCell.getY()],
                                        referencedCell.getX(),
                                        referencedCell.getY()) + 1;
                            }
                        }
                        return 0;
                    }
                    return -1;
                }
                return -1;
            }
            return 0;
        }


//CellEntry הפונקציה מקבלת מחרוזת וממירה אותה ל
        public static CellEntry whatTheCell(String str) {
                char[] characters = str.toCharArray();
                String cellReference = "";
                int lastLetterIndex = 0;

                // איסוף האותיות הגדולות במחרוזת
                for (int i = 0; i < characters.length; i++) {
                    if (characters[i] >= 'A' && characters[i] <= 'Z') {
                        cellReference += characters[i];
                        lastLetterIndex = i;
                    }
                }
                // איסוף הספרות במחרוזת
                for (int j = lastLetterIndex; j < characters.length; j++) {
                    if (SCell.isNumber(String.valueOf(characters[j]))) {
                        cellReference += characters[j];
                    }
                }

                return new CellEntry(cellReference);
            }}