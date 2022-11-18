package buildtree;

import org.eclipse.core.runtime.CoreException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ToTrueOrFalse  implements  Mutation{
    public ArrayList<Node> main(ArrayList<Node> list) throws IOException, CoreException {
        ArrayList<Node> reNode = new ArrayList<>();
        Random random = new Random();

        for (Node child: list) {
            int t = random.nextInt(1);
            toEqual(child,t);
            reNode.add(child);
        }

        return reNode;
    }

    public static void toEqual(Node node, int t){
        List<Node> sonList = node.getSonList();
        if(sonList == null || sonList.size() == 0){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {
                Node cur = sonList.get(i);
                if(cur.getNodeValue().indexOf("true") >= 0 && cur.getNodeName().indexOf("Statement") >= 0 && !cur.getNodeName().equals("CASTCompoundStatement") && t == 0){
                    String temp = cur.getNodeValue().replaceAll("true", "false");
                    cur.setNodeValue(temp);
                    changeSon(cur, "true");
                    continue;
                }else {
                    if(cur.getNodeValue().indexOf("false") >= 0 && cur.getNodeName().indexOf("Statement") >= 0 && !cur.getNodeName().equals("CASTCompoundStatement") && t == 0){
                        String temp = cur.getNodeValue().replaceAll("false", "true");
                        cur.setNodeValue(temp);
                        changeSon(cur, "false");
                        continue;
                    }
                }
                toEqual(sonList.get(i), t);
            }
        }
    }

    public static void changeSon(Node node, String s){
        List<Node> sonList = node.getSonList();
        if(sonList == null || sonList.size() == 0){
            return;
        }else {
            for (int i = 0; i < sonList.size(); i++) {
                Node cur = sonList.get(i);
                if(cur.getNodeValue().indexOf(s) >= 0 && s.equals("true")){
                    String temp = cur.getNodeValue().replaceAll(s, "false");
                    cur.setNodeValue(temp);
                }else {
                    if(cur.getNodeValue().indexOf(s) >= 0 && s.equals("false")){
                        String temp = cur.getNodeValue().replaceAll(s, "true");
                        cur.setNodeValue(temp);
                    }
                }
                changeSon(sonList.get(i), s);
            }
        }
    }
}
