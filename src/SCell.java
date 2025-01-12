public class SCell implements Cell {
    private String line;
    private int type;
    private int order;

    public SCell(String s) {
        setData(s);
    }
    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String str) {
        line = str;
        char[] arr = str.toCharArray();

        if (str.isEmpty())
            setType(1);
        else if (isNumber(str))
            setType(2);
        else if (arr[0] == '=') {
            if (isForm(str))
                setType(Ex2Utils.FORM);
            else {
                setType(-2);
            }
        } else {
            setType(1);
        }
    }

    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        this.order = t;
    }

    //פונגציה שבודקת האם המחרוזת היא מספר
    public static boolean isText(String a) {
        if (a == null || a.isEmpty()) {
            return false;
        }
        for (int i = 0; i < a.length(); i++) {
            if (!Character.isLetter(a.charAt(i))) {// בדיקה אם המחרוזת מכילה רק אותיות
                return false;
            }
        }
        return true;
    }
//פונגציה שבודקת האם המחרוזת היא מספר
    public static boolean isNumber(String a) {
            if (a == null || a.isEmpty()) {
                return false;
            }
            int Index1 = 0;
            if (a.charAt(0) == '+' || a.charAt(0) == '-') {
                if (a.length() == 1) {
                }
                Index1 = 1;
            }
            boolean hasDecimalPoint = false;

            for (int i = Index1; i < a.length(); i++) {
                char c = a.charAt(i);
                if (c == '.') {
                    if (hasDecimalPoint) {
                        return false;
                    }
                    hasDecimalPoint = true;
                } else if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
    }
    // פונגציה שבודקת אם המחרוזת  תיקנית (ונעזרת בפונקציה נוספת)
    public static boolean isForm(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        char[] a = text.toCharArray();
        if (a[0] == '=' && a.length==1) {
            return false;
        }
        if (a[0] != '=') {
            return false;
        }
        if(a[1]=='-'||a[1]=='+')
            text=text.substring(1);

        return isFormHelper(text.substring(1));
    }
    // פונגציה התומכת בבדיקה של הנוסחא ( פונגציית עזר)
    public static boolean isFormHelper(String text) {
            if (text.isEmpty()) {
                return false;
            }
            if (isNumber(text)) {
                return true;
            }
            if (!SCell.ifOpTwins(text)) {
                return false;
            }
            char[] arr = text.toCharArray();
            if (arr[0] == '*' || arr[0] == '/') {
                return false;
            }
            if ("+-*/".indexOf(arr[arr.length - 1]) != -1) {
                return false;
            }
            // בדיקת תוכן בתוך סוגריים תקינים
            if (arr[0] == '(' && arr[arr.length - 1] == ')') {
                return isFormHelper(text.substring(1, text.length() - 1));
            }
            // בדיקה עבור משתנים בפורמט A1 עד I16
            if (arr[0] >= 'A' && arr[0] <= 'I') {
                if (isNumber(Character.toString(arr[1]))) {
                    if (isNumber(text.substring(1))) {
                        int n = Integer.parseInt(text.substring(1));
                        if (n >= 0 && n <= 16) {
                            return true;
                        }
                    }
                } else {
                    return false;
                }
            }
            // בדיקה רקורסיבית עם ספירת סוגריים
            int count = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] == '(') {
                    count++;
                } else if (arr[i] == ')') {
                    count--;
                }

                // אם תו הוא סימן פעולה מחוץ לסוגריים, בדוק את שני הצדדים שלו
                if ("+-*/".indexOf(arr[i]) != -1 && count == 0) {
                    return isFormHelper(text.substring(0, i)) && isFormHelper(text.substring(i + 1));
                }
            }
            return false;
        }
    //הפונקציה בודקת אם במחרוזת הנתונה אין 2 סמני פעולה עוקבים
    public static boolean ifOpTwins(String str){
        String operators = "+-/* ";
        for (int i = 0; i < str.length() - 1; i++) {
            if (operators.indexOf(str.charAt(i)) != -1 && operators.indexOf(str.charAt(i + 1)) != -1) {
                return false;
            }
        }
        return true;
    }

    //פעולה שמחלקת את האפרטורים למשקלים (שמחוץ לסוגריים )
    //כפל וחילוק יקבל משקל קטן יותר מחיבור וחיסור משום שנבצע אותם ראשון
    public static double[] opValue(String input) {
            if (!input.contains("+") && !input.contains("-") && !input.contains("*") && !input.contains("/")) {
                return null;
            }
            // הצהרה על משתנים על מנת לעקוב אחרי סוגריים וסימני פעולה
            int bracketCount = 0;
            char[] charArray = input.toCharArray();
            double[] operatorValues = new double[charArray.length];

            for (int index = 0; index < charArray.length; index++) {
                char currentChar = charArray[index];

                if (currentChar == '(') {
                    bracketCount++;
                }

                else if (currentChar == ')') {
                    bracketCount--;
                }

                if (bracketCount == 0) {

                    if (currentChar == '*' || currentChar == '/') {
                        operatorValues[index] += 1;
                    }

                    else if (currentChar == '+' || currentChar == '-') {
                        operatorValues[index] += 2;
                    }
                }
            }

            return operatorValues;
        }


//הפונקציה בודקת אם המחרוזת מתחילה ב-=. אם כן, היא מסירה את הסימן הזה ומעבירה את המחרוזת לפונגציה אחרת לחישוב
    public static double helpComputeForm(String str) {
        char[] c = str.toCharArray();

        if (c[0] == '=')
            str = str.substring(1);
        return computeForm(str);
    }
//הפונקציה מבצעת חישוב של ביטוי מתמטי
        public static double computeForm(String text) {
            char[] arr = text.toCharArray();
            if(arr[0]=='-'){
                text='-'+""+computeForm(text.substring(1));
            }

            if(isNumber(text))
                return Double.parseDouble(text);

            while (arr[0] == '(' && arr[arr.length - 1] == ')' && isFormHelper(text.substring(1, text.length() - 1))){
                text = text.substring(1, arr.length - 1);
                arr = text.toCharArray();

            }
            if(isNumber(text))
                return Double.parseDouble(text);

            String st="";
            for(int i=0;i<arr.length;i++) {
                if(i<arr.length-2){
                    if ((arr[i] == '-' && arr[1 + i] == '-') || (arr[i] == '+' && arr[1 + i] == '+') || (arr[i] == '+' && arr[i + 1] == '-') || (arr[1] == '-' && arr[1 + i] == '+')) {
                        int s=0;
                        if (arr[i] == '-')
                            st += "(-1)*";
                        else if (arr[i] == '+')
                            st += "1*";
                        if (arr[i + 1] == '-') {
                            st += "(-1)*";
                            s++;
                        } else if (arr[i + 1] == '+') {
                            st += "1*";
                            s++;
                        }
                        if(arr[i+2] == '-' || arr[2 + i] == '+'){
                            if (arr[i + 2] == '-') {
                                st += "(-1)*";
                                s++;
                            } else if (arr[i + 2] == '+') {
                                st += "1*";
                                s++;
                            }
                        }
                        i=s;

                    } else st += arr[i];
                } else st += arr[i];

            }
            text=st;
            arr=text.toCharArray();

            double d = 0;
            int count = 0;
            double[] arr1 = opValue(text);
            if (arr1 != null) {
                double max = 0;
                count = 0;
                for (int i = 0; i < arr1.length; i++) {
                    if (arr1[i] >= max) {
                        max = arr1[i];
                        count = i;
                    }
                }
                if (arr[count] == '+')
                    return computeForm(text.substring(0, count)) + computeForm(text.substring(count + 1));

                else if (arr[count] == '*') {
                    return computeForm(text.substring(0, count)) * computeForm(text.substring(count + 1));
                } else if (arr[count] == '/') {
                    return computeForm(text.substring(0, count)) / computeForm(text.substring(count + 1));

                } else if (arr[count] == '-') {
                    return computeForm(text.substring(0, count)) - computeForm(text.substring(count + 1));

                }
            }

            return Double.parseDouble(text);
        }

    }



