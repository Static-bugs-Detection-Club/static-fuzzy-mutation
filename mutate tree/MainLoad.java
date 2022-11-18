package buildtree;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.eclipse.core.runtime.CoreException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainLoad {
    public static void main(String[] args) throws IOException, CoreException {
        String path = "D:\\论文资料\\UAF(1)\\code\\c++\\";

        getFileName(path);


//        for (int i = 0; i < nodes2.size(); i++) {
//            String curPath = ASTPath + fileName + "\\";
//            String curName = fileName + i;
//            File file = new File(curPath);
//            if(!file.exists())
//                file.mkdirs();
//            TreeUtils.log(nodes2.get(i).getNodeName() + " " + nodes2.get(i).getNodeValue(), curPath + curName + ".txt");
//            TreeUtils.queryAll(nodes2.get(i), 1, curPath + curName + ".txt");
//        }
    }

    public static void printTree(String path, String fileName) throws IOException, CoreException {
        ArrayList<Node> nodes = AddThread.main(path, fileName);

        Mutation mutation1 = new AddLock();
        ArrayList<Node> nodes0 = mutation1.main(nodes);
        String path0 = "D:\\论文资料\\mutation\\AddLock";
        for (int i = 0; i < nodes0.size(); i++) {
            String curPath = path0 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes0.get(i), curPath + curName +".txt");
        }


        Mutation mutation2 = new AddSignal();
        ArrayList<Node> nodes1 = mutation2.main(nodes);
        String path1 = "D:\\论文资料\\mutation\\AddSignal";
        for (int i = 0; i < nodes1.size(); i++) {
            String curPath = path1 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes1.get(i), curPath + curName +".txt");
        }

        Mutation mutation3 = new ToGlobal();
        ArrayList<Node> nodes2 = mutation3.main(nodes);
        String path2 = "D:\\论文资料\\mutation\\AddLock";
        for (int i = 0; i < nodes2.size(); i++) {
            String curPath = path2 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes2.get(i), curPath + curName +".txt");
        }

        Mutation mutation4 = new ReplaceJoin();
        ArrayList<Node> nodes3 = mutation4.main(nodes);
        String path3 = "D:\\论文资料\\mutation\\rePlaceJion";
        for (int i = 0; i < nodes3.size(); i++) {
            String curPath = path3 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes3.get(i), curPath + curName +".txt");
        }

        Mutation mutation5 = new ToDoublePlusOrDe();
        ArrayList<Node> nodes4 = mutation5.main(nodes);
        String path4 = "D:\\论文资料\\mutation\\ToAndOr";
        for (int i = 0; i < nodes4.size(); i++) {
            String curPath = path4 + "\\";;
            String curName = fileName + i;
            createJsonFile(nodes4.get(i), curPath + curName +".txt");
        }

        Mutation mutation6 = new ToUnEqual();
        ArrayList<Node> nodes5 = mutation6.main(nodes);
        String path5 = "D:\\论文资料\\mutation\\toDoublePlusOrDe";
        for (int i = 0; i < nodes5.size(); i++) {
            String curPath = path5 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes5.get(i), curPath + curName +".txt");
        }

        Mutation mutation7 = new ToSubtract();
        ArrayList<Node> nodes6 = mutation7.main(nodes);
        String path6 = "D:\\论文资料\\mutation\\toEqual";
        for (int i = 0; i < nodes6.size(); i++) {
            String curPath = path6 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes6.get(i), curPath + curName +".txt");
        }

        Mutation mutation8 = new ToEqual();
        ArrayList<Node> nodes7 = mutation8.main(nodes);
        String path7 = "D:\\论文资料\\mutation\\toGlobal";
        for (int i = 0; i < nodes7.size(); i++) {
            String curPath = path7 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes7.get(i), curPath + curName +".txt");
        }

        Mutation mutation9 = new ToAndOr();
        ArrayList<Node> nodes8 = mutation9.main(nodes);
        String path8 = "D:\\论文资料\\mutation\\ToIfFalse";
        for (int i = 0; i < nodes8.size(); i++) {
            String curPath = path8 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes8.get(i), curPath + curName +".txt");
        }

        Mutation mutation10 = new ToTrueOrFalse();
        ArrayList<Node> nodes9 = mutation10.main(nodes);
        String path9 = "D:\\论文资料\\mutation\\ToOneOrTwoAnd";
        for (int i = 0; i < nodes9.size(); i++) {
            String curPath = path9 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes9.get(i), curPath + curName +".txt");
        }

        Mutation mutation11 = new ToIfFalse();
        ArrayList<Node> nodes10 = mutation11.main(nodes);
        String path10 = "D:\\论文资料\\mutation\\ToSubtract";
        for (int i = 0; i < nodes10.size(); i++) {
            String curPath = path10 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes10.get(i), curPath + curName +".txt");
        }

        Mutation mutation12 = new ToOneOrTwoAnd();
        ArrayList<Node> nodes11 = mutation12.main(nodes);
        String path11 = "D:\\论文资料\\mutation\\ToTrueOrFalse";
        for (int i = 0; i < nodes11.size(); i++) {
            String curPath = path11 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes11.get(i), curPath + curName +".txt");
        }

        Mutation mutation13 = new ToGreater();
        ArrayList<Node> nodes12 = mutation13.main(nodes);
        String path12 = "D:\\论文资料\\mutation\\ToUnEqual";
        for (int i = 0; i < nodes12.size(); i++) {
            String curPath = path12 + "\\";
            String curName = fileName + i;
            createJsonFile(nodes12.get(i), curPath + curName +".txt");
        }
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
                //FifthMutation.getFifthMutation(path+fs.getName(), fs.getName());
                //printTree(path + fs.getName(), fs.getName().substring(0, fs.getName().indexOf(".")));

            }

        }

    }
    public static boolean createJsonFile(Object jsonData, String filePath) {
        String content = JSON.toJSONString(jsonData, SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteDateUseDateFormat);
        try {
            File file = new File(filePath);
            // 创建上级目录
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 如果文件存在，则删除文件
            if (file.exists()) {
                file.delete();
            }
            // 创建文件
            file.createNewFile();
            // 写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
            write.write(content);
            write.flush();
            write.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
