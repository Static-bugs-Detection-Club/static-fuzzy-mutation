package buildtree;

import org.apache.commons.lang3.SerializationUtils;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandomDelete {
    static int counts = 0;
    static int count = 0;
    static boolean aBoolean = false;
    public static void main(String[] args) throws IOException, CoreException {
        String path = "D:\\论文资料\\LinuxUaf1\\usbnet.txt";
//        ArrayList<ArrayList<Integer>> arrayLists;
//        for (int i = 1; i < 10; i++) {
//            arrayLists = AddThread.combine(10 , i);
//            System.out.println(arrayLists);
//            arrayLists.clear();
//        }
        //getFileName(path);
        main(path,"usbnet");
        
        //System.out.println(path.indexOf("abc"));

    }
    public static ArrayList<Node> main(String path, String str) throws CoreException, IOException {
        Node node = BuildTree.getTree(path);
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<ArrayList<Integer>> arrayLists;
        getStatement(node, strings);
        int statementNum = strings.size();
        String filePath = "D:\\论文资料\\LinuxResult\\" + str;
        File file = new File(filePath);
//        if(!file.exists()){
//            file.mkdir();
//        }
        for (int i = 1; i <= statementNum; i++) {
            arrayLists = AddThread.combine(statementNum, i);
            for (int j = 0; j < arrayLists.size(); j++) {
                ArrayList<Integer> arrayList = arrayLists.get(j);
                Node tepNode = SerializationUtils.clone(node);
                String curName = str;
                count++;
                for (int k = 0; k < arrayList.size(); k++) {

                    curName = curName + arrayList.get(k);
                    deleteStatement(tepNode, strings.get(arrayList.get(k)-1));
                }
                aBoolean = false;
                isFreeStatement(tepNode);
                if(aBoolean){
                    counts++;
                    System.out.println(counts);
                }
                //String curPath = filePath + "\\";

                //MainLoad.createJsonFile(tepNode, curPath + curName +".txt");
                //TreeUtils.log(nodes2.get(i).getNodeName() + " " + nodes2.get(i).getNodeValue(), curPath + curName + ".txt");
//                TreeUtils.queryAll(tepNode, 1, curPath + curName + ".txt");

            }
            arrayLists.clear();
        }
        System.out.println(count);
        System.out.println(counts);
        return null;
    }

    public static void deleteStatement(Node temNode, String string){
        List<Node> sonList = temNode.getSonList();
        if(sonList == null || sonList.isEmpty()){
            return;
        }else{
            for (int i = 0; i < sonList.size(); i++) {
                if(sonList.get(i).getId().equals(string)){
                    sonList.remove(i);
                    break;
                }else {
                    deleteStatement(sonList.get(i), string);
                }
            }
        }
    }

    public static void isFreeStatement(Node node){
        List<Node> sonList = node.getSonList();

        if(sonList == null || sonList.isEmpty()){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {
                if (sonList.get(i).getNodeName().equals("CASTFunctionCallExpression")  && (sonList.get(i).getNodeValue().indexOf("Free") >= 0 || sonList.get(i).getNodeValue().indexOf("free") >= 0)) {
                    aBoolean = true;
                }else {
                    isFreeStatement(sonList.get(i));
                }
            }
        }

    }
    public static void getStatement(Node node, ArrayList<String> strings){
        List<Node> sonList = node.getSonList();
        if(sonList == null || sonList.isEmpty()){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {
                if (sonList.get(i).getNodeName().indexOf("CompoundStatement") == -1 && sonList.get(i).getNodeName().indexOf("Statement") >= 0) {
                    strings.add(sonList.get(i).getId());
                }else {
                    getStatement(sonList.get(i), strings);
                }
            }
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
                main(path + fs.getName(), fs.getName().substring(0, fs.getName().indexOf(".")));

            }

        }

    }
}
