package buildtree;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PrintTree {
    public static void main(String[] args) throws IOException, CoreException {
        String path = "D:\\论文资料\\实验数据\\数据";
        getFileName(path);
    }

    public static void main(String path, String str, String path1) throws CoreException, IOException {
        Node node = BuildTree.getTree(path);

        String filePath = path1 + "AST";
        File file = new File(filePath);
        if(!file.exists()){
            file.mkdir();
        }

         String curPath = filePath + "\\";

         //MainLoad.createJsonFile(tepNode, curPath + curName +".txt");
         TreeUtils.log(node.getNodeName() + " " + node.getNodeValue(), curPath + str + ".txt");
         TreeUtils.queryAll(node, 1, curPath + str + ".txt");


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
                getFileName(path +"\\" + fs.getName()+"\\");
            } else {
                //FifthMutation.getFifthMutation(path+fs.getName(), fs.getName());
                main(path + fs.getName(), fs.getName().substring(0, fs.getName().indexOf(".")), path);
            }

        }

    }
}
