import java.io.*;
import java.util.*;

public class wc {
    public static String get_character_count(String filename)throws IOException{
        //直接通过read（）函数读取字符数
        FileReader f_obj = new FileReader(filename);
        BufferedReader b_obj = new BufferedReader(f_obj);
        int flag;
        int character_num = 0;
        String result;
        while( (flag = b_obj.read()) != -1){
            character_num ++;
        }
        result =filename + ",字符数：" +  character_num + "\r\n";
        return result;
    }

    public static List<String> getStopList(BufferedReader buff) throws IOException {
        List<String> lists = new ArrayList<String>();
        String str;
        while ((str = buff.readLine()) != null) {
            lists.addAll(Arrays.asList(str.split(" ")));
        }
        return lists;
    }

    public static List<String> get_stop(String args[])throws IOException{
        //包含“-e”时，获取stoplist中的单词作为停用词
        List<String> input_args = Arrays.asList(args);
        List<String> stop_words = new ArrayList<String>();
        if (input_args.contains("-e")) {
            int index = input_args.indexOf("-e");
            String stopFile = input_args.get(index + 1);
            BufferedReader buffer = null;
            buffer = new BufferedReader(new FileReader(stopFile));
            stop_words = getStopList(buffer);
        }
        return stop_words;
    }

  public static int get_stop_num(String filename,List<String> stop_list)throws IOException{
      //获取停用词数目
      FileReader f_obj = new FileReader(filename);
      BufferedReader b_obj = new BufferedReader(f_obj);
      String str;
      int num_stop=0;
      while ((str = b_obj.readLine()) != null) {
          for (String s : str.split(" ")) {
              for (String s1 : s.split(",")) {
                  if (s1.length() > 0 && stop_list.contains(s1)) {
                      num_stop++;
                  }
              }
          }
      }
      return num_stop;
  }

    public static int get_word_count(String filename)throws IOException{
        //获取单词数
        FileReader f_obj = new FileReader(filename);
        BufferedReader b_obj = new BufferedReader(f_obj);
        int flag;
        int word_num = 0;
        boolean first_count = true;
        while((flag = b_obj.read()) != -1){
            if (first_count){
                if(flag != ',' && flag != ' ' && flag != '\n' && flag !='\r' && flag != '\t')
                    word_num += 1;
                first_count = false;
            }

            if (flag == ',' || flag == ' ' || flag == '\n'||flag =='\r' || flag == '\t' ){
                first_count = false;
                while((flag = b_obj.read()) != -1 ){
                    if (flag != ',' && flag != ' '&& flag != '\n' && flag !='\r' && flag != '\t'){
                        word_num ++;
                        break;
                    }
                }
            }
        }
        return word_num;
    }

    public static String  get_line_count(String filename)throws IOException{
        //用readline（）函数获取行数
        FileReader f_obj = new FileReader(filename);
        BufferedReader b_obj = new BufferedReader(f_obj);
        int line_num = 0;
        String result;
        String str_line = null;
        while ((str_line = b_obj.readLine()) != null){
            line_num ++;
        }
        result =filename + ",行数：" +  line_num + "\r\n";
        return result;
    }

    public static String get_line_info(String filename)throws IOException{
        //统计代码行、空行、注释行数目
        int blank_line_num =0,code_line_num = 0,note_line_num = 0;
        FileReader f_obj = new FileReader(filename);
        String result;
        BufferedReader b_obj = new BufferedReader(f_obj);
        int line_num = 0;
        String str_line = null;
        while ((str_line = b_obj.readLine()) != null) {
            if (str_line.length() == 0 || str_line.equals("{" )|| str_line.equals("}")) {
                blank_line_num++; //统计空行
            } else {
                for (int i = 0; i < str_line.length()-1; i++) {
                    boolean flag2=true;
                    while (str_line.charAt(i) != ' ' && str_line.charAt(i) != '\t' && flag2) {
                        if (str_line.charAt(i) == '/' && str_line.charAt(i + 1) == '/') {
                            note_line_num++; //统计//形式的注释行
                            i = str_line.length();
                            break;
                        } else if (str_line.charAt(i) == '/' && str_line.charAt(i + 1) == '*') {
                            boolean flag=true;//统计形如/**/的注释行数目
                            for (int t = str_line.length()-1; t >= 0; t--) {
                                while (str_line.charAt(t) != ' ' && str_line.charAt(t) != '\t'&& t>=1) {
                                    if (str_line.charAt(t) == '/' && str_line.charAt(t - 1) == '*') {
                                        t = -1;    //本行末尾结束 直接加1 并不进行后续代码
                                        flag =false;
                                        flag2=false;
                                        break;
                                    }
                                    else break;
                                }
                            }
                            note_line_num +=1;
                            while (flag) {  //非本行结束 往下判断 找到代码行结束位置
                                if((str_line = b_obj.readLine()) != null) {
                                    note_line_num++;
                                    line_num++;
                                    for (int j = str_line.length() - 1; j >= 0; j--) {
                                        while (str_line.charAt(j) != ' ' && str_line.charAt(j) != '\t' && j >= 1) {
                                            if(str_line.charAt(0) == '*' && str_line.charAt(j - 1) == '/' && str_line.length() > 2) {
                                                note_line_num -= 1;
                                                j = -1;
                                                flag = false;
                                                break;

                                            }
                                            else if (str_line.charAt(j) == '/' && str_line.charAt(j - 1) == '*') {
                                                j = -1;
                                                flag = false;
                                                break;
                                            }
                                            else break;
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            break;
                        }
                    }
                }
            }
            line_num++;  //统计总代码行数
        }
        code_line_num = line_num - note_line_num -blank_line_num;  //代码行 = 总行数 - 空行 -注释行
        result = filename +",代码行/空行/注释行："+ code_line_num +"/" + blank_line_num + "/" +note_line_num + "\r\n";
        return result;
    }

    public static void get_result(BufferedWriter file_writer,String result) throws IOException{
        file_writer.write(result);  //将结果写入指定文件
        file_writer.flush();
    }

    public static void out_result(String filename,BufferedWriter file_writer,String args[],int args_len )throws IOException{
        String character_num= "",line_num="",line_info="", word_num = ""; //判定执行哪些命令
        int word_numb = -1;
        String arg;
        for (int i=0; i<args_len; i++){
            arg = args[i];
            switch (arg){
                case "-c":
                    character_num = get_character_count(filename);
                    break;
                case "-w":
                    word_numb = get_word_count(filename);
                    break;
                case "-l":
                    line_num = get_line_count(filename);
                    break;
                case "-a":
                    line_info = get_line_info(filename);
                    break;
                case "-e":
                    List<String> stop_list = get_stop(args);
                    int num_stop = get_stop_num(filename,stop_list);
                    word_numb -= num_stop;
            }
        }
        if(word_numb >=0){
            word_num = filename + ",单词数："+ word_numb +"\r\n";
        }
        get_result(file_writer,character_num);
        get_result(file_writer,word_num);
        get_result(file_writer,line_num);
        get_result(file_writer,line_info);
    }
    public static List<String> searchFile(String absPath, String lastPath) {//递归遍历当前目录以及子目录下符合要求的文件
        String regex = lastPath.replace("?", "[0-9a-z]");
        regex = regex.replace("*", "[0-9a-z]{0,}"); //正则匹配
        List<String> matchFile = new ArrayList<String>();
        File file = new File(absPath);
        File[] files = file.listFiles();//获取当前路径下所有文件
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                List<String> matchs = searchFile(files[i].getAbsolutePath(), lastPath);
                matchFile.addAll(matchs);
            } else if (files[i].isFile()) {
                String filename = files[i].getName();
                if (filename.matches(regex))
                    matchFile.add(files[i].getAbsolutePath());
            }
        }
        return matchFile;
    }


    public static void main(String[] args)throws IOException{
        String exepath=System.getProperty("exe.path"); //获取exe文件的当前目录
        exepath =exepath.substring(0,exepath.length()-1);

       //String exepath = "E:\\IDEA项目\\WCProject\\WCProject";
        int args_len = args.length;
        String result_filename,filename="";

        if(args_len < 2) {
            System.out.println("please input right command");
            return;
        }
        List<String> input_str = Arrays.asList(args);

        if (input_str.contains("-s")) {
            int tmp=0;
            int length = input_str.size();
            for (int i = 1; i < args_len; i++) {

                if (args[i].indexOf(".c") != -1) {
                    tmp = i;
                }
            }
                filename = input_str.get(tmp);
                List<String> filelist = new ArrayList<String>();
                //判断输入文件为绝对路径还是相对路径，如果filename中包含字符"/"，则说明是绝对路径
                if (filename.indexOf("\\") > 0) {
                    int index = filename.lastIndexOf("\\");
                    String absPath = filename.substring(0, index + 1);
                    String lastPath = filename.substring(index + 1);
                    filelist = searchFile(absPath, lastPath); //找到所有*.c文件
                } else {
                    filelist = searchFile(exepath, filename);
                }
                if (input_str.indexOf("-o") > 0) { //存在“-o”时 写入文件夹为后面跟的文件夹
                    result_filename = args[input_str.indexOf("-o") + 1];
                } else {
                    result_filename = "result.txt"; //默认为result.txt
                }
                BufferedWriter file_writer = new BufferedWriter(new FileWriter(result_filename));
                for (String s : filelist) {
                    int index = s.lastIndexOf("\\");//找到最后一个\\位置 前面为文件目录名 后面是文件名
                    String absPath = s.substring(0, index + 1);
                    String lastPath = s.substring(index + 1);
                    out_result(lastPath, file_writer, args, args_len);
                }
                if (file_writer != null)
                    file_writer.close();
            }
        else {
            if (input_str.indexOf("-o") >0){//存在“-o”时 写入文件夹为后面跟的文件夹
                result_filename = args[input_str.indexOf("-o") + 1];
                String t="";
                for (int i=1; i<args_len; i++) {

                    if (args[i].indexOf(".c") != -1) {
                      t= args[i];
                    }
                    filename =t;
                }
            }
            else {
                String t="";
                for (int i=1; i<args_len; i++) {

                    if (args[i].indexOf(".c") != -1) {
                         t = args[i];
                    }
                    filename =t;
                }
                result_filename = "result.txt";
            }
            BufferedWriter file_writer = new BufferedWriter(new FileWriter(result_filename));
            out_result(filename,file_writer,args,args_len);
            if (file_writer != null)
                file_writer.close();
        }
    }

}