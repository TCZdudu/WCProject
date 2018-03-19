import java.io.*;

public class wc {
    public static int get_character_count(String filename)throws IOException{
        FileReader f_obj = new FileReader(filename);
        BufferedReader b_obj = new BufferedReader(f_obj);
        int flag;
        int character_num = 0;
        while( (flag = b_obj.read()) != -1){
            character_num ++;
        }
        return character_num;
    }

    public static int get_word_count(String filename)throws IOException{
        FileReader f_obj = new FileReader(filename);
        BufferedReader b_obj = new BufferedReader(f_obj);
        int flag;
        int word_num = 0;
        boolean first_count = true;
        while((flag = b_obj.read()) != -1){
            if (first_count){
                if(flag != ',' && flag != ' ' && flag != '\n')
                    word_num += 1;
                first_count = false;
            }

            if (flag == ',' || flag == ' ' || flag == '\n'){
                while((flag = b_obj.read()) != -1 && flag != '\n'){
                    if (flag != ',' && flag != ' '){
                        word_num ++;
                        break;
                    }
                }
            }
        }
        return word_num;
    }

    public static int get_line_count(String filename)throws IOException{
        FileReader f_obj = new FileReader(filename);
        BufferedReader b_obj = new BufferedReader(f_obj);
        int line_num = 0;
        String str_line = null;
        while ((str_line = b_obj.readLine()) != null){
            line_num ++;
        }
        return line_num;
    }

    public static void get_result(BufferedWriter file_writer,String result) throws IOException{
        file_writer.write(result);
        file_writer.flush();
        file_writer.newLine();
    }

    public static void main(String[] args)throws IOException{
        int character_num,line_num,word_num;
        int args_len = args.length;
        if(args_len < 2) {
            System.out.println("please input right command");
            return;
        }
        String result_filename,filename;
        if (args[args_len - 2].equals("-o")){
            result_filename = args[args_len - 1];
            filename = args[args_len - 3];
        }
        else {
            filename = args[args_len - 1];
            result_filename = "result.txt";
        }
        BufferedWriter file_writer = new BufferedWriter(new FileWriter(result_filename));
        String arg,result;
        for (int i=0; i<args_len; i++){
            arg = args[i];
            switch (arg){
                case "-c":
                    character_num = get_character_count(filename);
                    result =filename + ",字符数：" +  character_num + "\n";
                    get_result(file_writer,result);
                    break;
                case "-w":
                    word_num = get_word_count(filename);
                    result = filename + ",单词数：" + word_num + "\n";
                    get_result(file_writer,result);
                    break;
                case "-l":
                    line_num = get_line_count(filename);
                    result = filename + ",行数："+ line_num + "\n";
                    get_result(file_writer,result);
                    break;
            }
        }
        if (file_writer != null)
            file_writer.close();
    }

}