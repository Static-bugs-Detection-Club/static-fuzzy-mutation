package buildtree;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.eclipse.core.runtime.CoreException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class RandomMutation {
    public static void main(String[] args) throws IOException, CoreException {
        String path = "D:\\论文资料\\UAF(1)\\code\\exCode\\";
        //String fileName = "102226bad";
        int[] ints = {0, 1, 2, 3,4,5,6,7,8};
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (Integer i: ints) {
            arrayList.add(i);
        }
        //LinkedList<Mutation> mutations = SelectMutation.selectMutation(arrayList);
        getFileName(path, arrayList);
        //ranMutation(path, fileName, mutations);

    }

    public static void ranMutation(String path, String fileName, LinkedList<Mutation> mutations) throws IOException, CoreException {

        String resultPath = "D:\\论文资料\\变异\\resultJson\\";
        String ASTPath = "D:\\论文资料\\变异\\AST\\";
        ArrayList<Node> nodes = AddThread.main(path, fileName);
//        for (Mutation m: mutations) {
//            nodes = m.main(nodes);
//        }
//        for (int i = 0; i < nodes.size(); i++) {
//            String curPath = resultPath+fileName + "\\";
//            File file = new File(curPath);
//            if(!file.exists())
//                file.mkdirs();
//            String curName = fileName + i;
//            createJsonFile(nodes.get(i), curPath + curName +".txt");
//        }

//        for (int i = 0; i < nodes.size(); i++) {
//            String curPath = ASTPath + fileName + "\\";
//            String curName = fileName + i;
//            File file = new File(curPath);
//            if(!file.exists())
//                file.mkdirs();
//            TreeUtils.log(nodes.get(i).getNodeName() + " " + nodes.get(i).getNodeValue(), curPath + curName + ".txt");
//            TreeUtils.queryAll(nodes.get(i), 1, curPath + curName + ".txt");
//        }
    }

    public static void getFileName(String path, ArrayList<Integer> arrayList) throws IOException, CoreException {

        File f = new File(path);
        if (!f.exists()) {
            System.out.println(path + " not exists");
            return;

        }

        //int[] ints = {0, 1, 2, 3,4,5,6,7,8};
        //ArrayList<Integer> arrayList = SelectMutation.getWeight(ints, ints.length);
        LinkedList<Mutation> mutations = SelectMutation.selectMutation(arrayList);
        File[] fa = f.listFiles();
        for (int i = 0; i < fa.length; i++) {
            File fs = fa[i];
            if (fs.isDirectory()) {
                getFileName(path + fs.getName()+"\\", arrayList);
            } else {
                //FifthMutation.getFifthMutation(path+fs.getName(), fs.getName());
                System.out.println(path + fs.getName() +" " +fs.getName().substring(0,fs.getName().indexOf(".")));
                ranMutation(path + fs.getName(), fs.getName().substring(0,fs.getName().indexOf(".")), mutations);
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
