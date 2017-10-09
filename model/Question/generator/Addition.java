package application.model.Question.generator;

public class Addition {

    public Addition(int answer){

    }

    protected String generateQuestion(int answer) {
        //int range = (max - min) +1
        //int a = (int)(Math.random()* range + min)
        int a = (int)(Math.random() * (answer - 1) + 1);
        int b = answer - a;
        return " "+ a + " + " + b + " ";
    }

    public static class Multiplication {
        private enum Combo{



        }
    }

    public static class Power {
        private int[] powers = {1, 4, 8, 9, 16, 25, 27 ,32 ,36, 49, 64, 81 };


    }

    public static class Substraction extends Question {
        private final int MAX = 99;

        public Substraction(int answer) {
            super(answer);
        }

        protected String generateQuestion(int answer) {
            int a = (int)(Math.random() * (MAX - answer + 1 ) + 1);
            int b = a - answer;
            return " " + a + " - " + b + " ";
        }
    }

    public abstract static class Question {
        private String _question;
        private String _answer;

        public Question(int answer){
            _question = generateQuestion(answer);
            _answer = Integer.toString(answer);
        }

        protected abstract String generateQuestion(int answer);

        public String getAnswer(){
            return _answer;
        }

        public String getQuestion() {
            return _question;
        }

    /*
        public static void generateQuestionBank(){
            String file = QUESTIONBANK + "factors.txt";
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
                for (int i = 1; i<=99; i++){
                    String line = i+"=";
                    for (int j = 1; j<=i; j++){
                        if (i%j ==0){
                            line=line+j+"*"+ i/j+" ";
                        }
                    }
                    writer.write(line + "\n");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }

            PrintWriter writer = new PrintWriter(QUESTIONBANK + "factors.txt", "UTF-8");
            for (int i = 1; i<=99; i++){
                String line = i+"=";
                for (int j = 1; j<=i; j++){
                    if (i%j ==0){
                        line=line+j+"*"+ i/j+" ";
                    }
                }
                writer.println(line);
            }
            writer.close();

            writer = new PrintWriter(QUESTIONBANK + "divisors.txt", "UTF-8");
            for (int i = 1; i<=99; i++){
                String line = i+"=";
                for (int j = 99; j >= i; j--){
                    if (j%i == 0){
                        line=line+j + "/"+ j/i +" ";
                    }
                }
                writer.println(line);
            }
            writer.close();

            writer = new PrintWriter(QUESTIONBANK + "powers.txt", "UTF-8");
            for (int i = 2; i <= 9; i++){
                String line = i+"=";
                int j = 2;
                while ((int)Math.pow(i,j) < 100){
                    line = line + i+ "^" + j+" ";
                    j++;
                }
                writer.println(line);
            }
            writer.close();
        }
    */



    }
}