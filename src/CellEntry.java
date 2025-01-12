// Add your documentation below:
public class CellEntry  implements Index2D {

    private String cellE;

    public CellEntry(String str) {
        if(str==""){
            this.cellE = str;
        }
        else {
            char[]a=str.toCharArray();
            if(a[0]=='=')
                str=str.substring(1);
        }
        this.cellE = str;

    }

    @Override
    public boolean isValid() {//בודק אם התא חוקי
        if(cellE=="")
            return false;
        char[] a = cellE.toCharArray();
        if (('A' <= a[0] && 'Z' >= a[0] && SCell.isNumber(cellE.substring(1))) || ('a' <= a[0] && 'z' >= a[0] && SCell.isNumber(cellE.substring(1))))
        {
            int n = Integer.parseInt(cellE.substring(1));
            if (0 <= n && n <= 99)
            {
                return true;
            }
        }
        return false;

    }

    @Override
    public int getX() {// בדיקה על X
        String[] a = Ex2Utils.ABC;
        if(isValid()) {
            for (int i = 0; i < a.length; i++) {

                if (a[i].charAt(0) == cellE.charAt(0))
                    return i;
            }
        }
        return Ex2Utils.ERR;
    }

    @Override
    public int getY() {// בדיקה על Y
        if(isValid()){
            if (SCell.isNumber(cellE.substring(1))) {
                int n = Integer.parseInt(cellE.substring(1));

                if (n >= 0 && n <= 99)
                    return n;
            }
        }
        return Ex2Utils.ERR;
    }

    @Override
    public String toString() {
        return cellE;
    }
    }