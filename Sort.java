import java.util.Arrays;

public class Sort {

    public static void main(String[] args) {
        int[] arr = {4,5,7,1,12,4,67,1,3,8,55,34};
        mergeSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void mergeSort(int[] arr){
        int[] temp = new int[arr.length];
        getSort(arr, 0, arr.length - 1, temp);
    }

    public static void  getSort(int[] arr, int l, int h, int[] temp){
        if(l < h){
            int m = l + (h - l)/2;
            getSort(arr, l, m, temp);
            getSort(arr, m + 1, h, temp);
            merge(arr, l, m, h, temp);
        }
    }

    public static void merge(int[] arr, int l, int m, int h, int[] temp){
        int i = l;
        int j = m + 1;
        int t = 0;
        while(i <= m && j <= h){
            if(arr[i] <= arr[j]){
                temp[t++] = arr[i++];
            }else {
                temp[t++] = arr[j++];
            }
        }

        while (i <= m)
            temp[t++] = arr[i++];

        while (j <= h)
            temp[t++] = arr[j++];

        t = 0;
        //将temp中的元素全部拷贝到原数组中
        while(l <= h){
            arr[l++] = temp[t++];
        }
    }
}
