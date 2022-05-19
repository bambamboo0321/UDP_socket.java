import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class operator {
    public String name;
    public String time;
    public int count;
    public int cost_time;
    public operator(String n, String t, int cnt, int time){
        name = n;
        this.time = t;
        count = cnt;
        cost_time = time;
    }
    public operator(){
    }
    public int getLength() {
        return name.getBytes(StandardCharsets.UTF_8).length + time.getBytes(StandardCharsets.UTF_8).length+ 4 +4;
    }
    public byte[] getByte(){
        return (name+","+time+","+count+","+cost_time).getBytes(StandardCharsets.UTF_8);
    }

    public String getString(){
        return (name+","+time+","+count+","+cost_time);
    }
    public static void sortedByTime(List<operator> input) {
        for(int i=0;i<input.size();i++) {
            for(int j=0;j<input.size();j++) {
                if (input.get(i).cost_time < input.get(j).cost_time) {
                    operator temp;
                    temp = input.get(i);
                    input.set(i, input.get(j));
                    input.set(j, temp);
                }
            }
        }
    }
    public static int getRank(List<operator> input,String n) {
        operator.sortedByTime(input);
        for(int i=0;i<input.size();i++) {
            if(input.get(i).name == n) return i+1;
        }
        return -1;
    }
    public static void readFile(String fileName, List<operator> content) {
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] temp = data.split(",");
                operator tmp = new operator(temp[0], temp[1], Integer.parseInt(temp[2]), Integer.parseInt(temp[3]));
                content.add(tmp);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void writeFile(String fileName, List<String> content) throws IOException {
        File file = new File(fileName);
        FileWriter writer = new FileWriter(file);
        for(int i=0;i< content.size();i++) {
            writer.write(content.get(i)+"\r\n");
        }
        writer.flush();
        writer.close();
    }
    public static void toTxt(String fileName,List<operator> content) throws IOException {
        List<String> e = new ArrayList<>();
        for(int i=0;i< content.size();i++) {
            e.add(content.get(i).getString());
        }
        operator.writeFile(fileName,e);

    }
}


