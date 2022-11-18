import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileRead {
    public static String txt2String(File file) {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void main(String[] args) {
        //File file = new File("C:\\Users\\eclipse\\Desktop\\数据\\变异\\ASTResult\\");
        //System.out.println(txt2String(file));
        getFileName("C:\\Users\\eclipse\\Desktop\\数据\\变异\\ASTResult");
    }

    public static void getFileName(String path) {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;
        }
        File fa[] = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");
            } else {
                System.out.println(fs.getName());
            }
        }
    }
}
