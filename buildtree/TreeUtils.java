package buildtree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TreeUtils {

    public static void insert(Node changeNode, String fatherId, Node newNode) {
        if (fatherId.equals(changeNode.getId())) {
            List<Node> nodes = changeNode.getSonList();
            boolean s = true;
            for (Node child:  nodes) {
                if(child.getNodeName().equals(newNode.getNodeName()) && child.getNodeValue().equals(newNode.getNodeValue())){
                    s = false;
                }
            }
            if(s){
                nodes.add(newNode);
            }
            return;
        }

        List<Node> sonList = changeNode.getSonList();
        if (sonList == null || sonList.isEmpty()) {
            return;                            //若该结点 的子结点集合为空 返回
        } else {

            for (int i = 0; i < sonList.size(); i++) {
                insert(sonList.get(i), fatherId, newNode);
            }
        }
    }

    /**
     * 遍历结点 并打印. 同时按每个结点所在深度 在结点前打印不同长度的空格
     * @param changeNode    根结点
     * @param depth        结点深度：1
     */
    public static void queryAll(Node changeNode, int depth, String str) throws IOException {
        List<Node> sonList = changeNode.getSonList();
        String space = generateSpace(depth);    //根据深度depth,返回(depth*3)长度的空格

        if (sonList==null||sonList.isEmpty()){
            return;
        }

        for (int i = 0; i <sonList.size() ; i++) {
            log(space    //打印空格 和结点id，name
                    +sonList.get(i).getNodeName()+" "
                    +sonList.get(i).getNodeValue(), str);

            if(i==0){
                depth = depth+1;  //结点深度+1，每个for循环仅执行一次。作为子结点sonList.get(i)的深度
            }
            queryAll(sonList.get(i),depth, str);
        }
    }

    /**
     * 删除结点   注意:先判断 是否在删除 根结点. 若删除根结点,不必进入此方法 直接为null即可
     * @param changeNode 根结点
     * @param id         待删除结点id
     */
    public void delete(Node changeNode, String id) {
        List<Node> sonList = changeNode.getSonList();

        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {

            for (int i = 0; i < sonList.size(); i++) {

                if(id.equals(sonList.get(i).getId())){
                    sonList.remove(i);
                    delete(new Node(),id);
                    break;
                }else{
                    delete(sonList.get(i), id);
                }
            }
        }
    }

    /**
     * 根据结点id  修改结点 name       //同理可根据结点name修改结点id
     * @param changeNode 根结点
     * @param id         结点id
     * @param name       修改后的 新name
     */
    public void update(Node changeNode, String id, String name) {
        if (changeNode.getId().equals(id)){
            changeNode.setNodeName(name);
            return;
        }
        List<Node> sonList = changeNode.getSonList();
        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {
                update(sonList.get(i), id,name);
            }
        }
    }

    /**
     * 查询 某个结点 到根结点的路径
     * @param changeNode 根结点
     * @param name       待查找的结点 name
     * @param wayList    路径
     */
    public void queryWayById(Node changeNode, String name, ArrayList<String> wayList) {
        List<Node> sonList = changeNode.getSonList();

        wayList.add(changeNode.getNodeName());
        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {

                if(name.equals(sonList.get(i).getNodeName())){
                    for (int j = 0; j < wayList.size(); j++) {
                        System.out.print(wayList.get(j)+"->");
                    }
                    System.out.println(sonList.get(i).getNodeName());
                    break;
                }
                queryWayById(sonList.get(i), name, wayList);
            }
        }
    }

    //获取所有语句节点
    public static void getStatementNode(Node node, ArrayList<Node> nodes){
        List<Node> sonList = node.getSonList();

        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {
                if(sonList.get(i).getNodeName().indexOf("Statement") >=0 && !sonList.get(i).getNodeName().equals("CASTCompoundStatement")){
                    nodes.add(sonList.get(i));
                }
                getStatementNode(sonList.get(i),nodes);
            }
        }
    }

    //获取局部变量结点
    public static void getLocalNode(Node node, ArrayList<Node> nodes){
        List<Node> sonList = node.getSonList();

        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {
            for (int i = 0; i < sonList.size(); i++) {
                if(sonList.get(i).getNodeName().equals("CASTDeclarationStatement") && !(sonList.get(i).getNodeValue().indexOf("pthread") >=0)){
                    nodes.add(sonList.get(i));
                    continue;
                }
                getLocalNode(sonList.get(i),nodes);
            }
        }
    }

    public static void getFunNode(Node node, ArrayList<Node> nodes){
        List<Node> sonList = node.getSonList();

        if (sonList == null || sonList.isEmpty()) {
            return;
        } else {

            for (int i = 0; i < sonList.size(); i++) {

                if(sonList.get(i).getNodeName().equals("CASTFunctionDefinition")){
                    nodes.add(sonList.get(i));
                    continue;
                }
                getStatementNode(sonList.get(i),nodes);
            }
        }

    }



    //打印空格
    public static String generateSpace(int count) {
        count = count*3;
        char[] chs = new char[count];
        for(int i = 0; i < count; i++) {
            chs[i] = ' ';
        }
        return new String(chs);
    }

    private static boolean fileLog = true;

    public static OutputStream getOutputStream(String str) throws IOException {
        if (fileLog) {

            File file = new File(str + ".txt");
            if (!file.exists())
                file.createNewFile();
            return new FileOutputStream(file, true);
        } else {
            return System.out;
        }
    }

    public static void log(String info, String str) throws IOException {
        OutputStream out = getOutputStream(str);
        out.write(info.getBytes("utf-8"));
        out.write("\r\n".getBytes());
    }
}
