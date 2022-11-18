import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.IOException;

public class TestFifth {
    public static void main(String[] args) throws IOException, CoreException {
        String path = "D:\\Software\\WeChat\\document\\WeChat Files\\wxid_940lvqe41hqm22\\FileStorage\\File\\2021-10\\变异\\result\\";
        getFileName(path);

    }

    public static void getFileName(String path) throws IOException, CoreException {

        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;

        }

        File[] fa = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                getFileName(path + fs.getName()+"\\");
            } else {
                FifthMutation.getFifthMutation(path+fs.getName(), fs.getName());

            }

        }

    }
}
