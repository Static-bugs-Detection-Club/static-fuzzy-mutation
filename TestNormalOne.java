import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.IOException;

public class TestNormalOne {
    public static void main(String[] args) throws IOException, CoreException {
        String path = "D:\\论文资料\\数据\\变异\\test\\";
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
                NormalMutationOne.getNormalOne(path+fs.getName(), fs.getName());
            }

        }

    }
}
