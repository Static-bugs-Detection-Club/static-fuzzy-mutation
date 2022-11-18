import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TestForth {
    public static void main(String[] args) throws IOException, CoreException {
        String path = "C:\\Users\\eclipse\\Desktop\\数据\\变异\\code\\";
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
                ForthMutation.noFourMutation(path+fs.getName(), fs.getName());

            }

        }

    }

}