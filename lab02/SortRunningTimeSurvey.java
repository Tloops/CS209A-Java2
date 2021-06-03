package lab2;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

public class SortRunningTimeSurvey {
	//                                  Task Name           Function Name      run times upper
	static String[][] taskList = { { "InsertionSortTest", "insertionSortTime", "100000" },
			                       { "BubbleSortTest",    "bubbleSortTime",    "100000" }, 
			                       { "SelectionSortTest", "selectionSortTime", "100000" },
			                       { "QuickSortTest",     "quickSortTime",     "100000" },
			                       { "MergeSortTest",     "mergeSortTime",     "100000" },
			                       { "HeapSortTest",      "heapSortTime",      "100000" }};

	static String[] dataList = { "AscendingSequence", "DescendingSequence", "RandomSequence", "OutOfOrderSequence" };
	static int[] data = new int[100000001];

	public static void main(String[] args) {
		String osName = System.getProperty("os.name");
		System.out.println(osName);
		try {
			File xlsFile = new File("RunningTimeSurvey.xls");
			// Create a workbook
			WritableWorkbook workbook;
			workbook = Workbook.createWorkbook(xlsFile);
			Class<?> me = Class.forName(Thread.currentThread().getStackTrace()[1].getClassName());
			
			int maxExp = getMaxExp(taskList);

			for (int s = 0; s < dataList.length; s++) {
				// Create a worksheet
				WritableSheet sheet = workbook.createSheet("RunningTime_"+dataList[s], s);
				// the first row
				for (int j = 1, n = 1; j <= maxExp; j++) {
					n = 10 * n;
					sheet.addCell(new Label(j + 1, 0, "n = " + n));
				}
				for (int i = 0; i < taskList.length; i++) {
					// col row data
					sheet.addCell(new Label(0, i + 1, taskList[i][0]));
					sheet.addCell(new Label(1, i + 1, taskList[i][1]));
				}
				for (int i = 1; i <= maxExp; i++) {
					Method methodGenSeq = me.getMethod(dataList[s], int.class);
					methodGenSeq.invoke(null, (int) Math.pow(10, i));
					for (int j = 0; j < taskList.length; j++) {
						String[] taskInfo = taskList[j];
						Method method = me.getMethod(taskInfo[1], int.class);
						int upper = Integer.parseInt(taskInfo[2]);
						int n = (int) Math.pow(10, i);
						if (n > upper) {
							continue;
						}

						// run a sort program, get the run time
						Long time = (Long) method.invoke(null, n);
						// add the run time to the sheet
						// col row data
						sheet.addCell(new Label(i + 1, j + 1, time.toString()));
					}

				}
			}
			workbook.write();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getMaxExp(String[][] taskList){
		int maxExp = 0;
		for (int i = 0; i < taskList.length; i++){
			int temp = (int) Math.ceil(Math.log10(Integer.parseInt(taskList[i][2])));
			if(maxExp < temp){
				maxExp = temp;
			}			
		}
		return maxExp;	
	}
	
	public static void AscendingSequence(int n) {
		for (int i = 0; i < n; i++) {
			data[i] = i;
		}
		System.out.println("AscendingSequence "+n+"elements");
	}

	public static void DescendingSequence(int n) {
		for (int i = 0; i < n; i++) {
			data[i] = n - i;
		}
		System.out.println("DescendingSequence "+n+"elements");
		
	}

	public static void RandomSequence(int n) {
		for (int i = 0; i < n; i++) {
			data[i] = i;
		}
		// shuffle
		Random rnd = new Random();
		for (int i = n; i > 1; i--) {
			int j = rnd.nextInt(i);
			int temp = data[j];
			data[j] = data[i - 1];
			data[i - 1] = temp;
		}
		System.out.println("RandomSequence "+n+" elements");
		
	}

	public static void OutOfOrderSequence(int n) {
		for (int i = 0; i < n; i++) {
			data[i] = i;
		}
		// shuffle
		Random rnd = new Random();
		for (int t = 0; t < n * 0.1; t++) {
            int i = rnd.nextInt(n);
            int j = rnd.nextInt(n);
			int temp = data[j];
			data[j] = data[i];
			data[i] = temp;
		}
		System.out.println("OutOfOrderSequence "+n+"elements");
		
	}

	public static long insertionSortTime(int n) {
		int[] list = new int[n];
		System.arraycopy(data,0,list,0,n);
		long timeStart = System.currentTimeMillis();
		insertionSort(n, list);
		long timeEnd = System.currentTimeMillis();
		long timeCost = timeEnd - timeStart;
		return timeCost;
	}

	public static void insertionSort(int n, int[] list) {
		// TODO :add your code here
		// reference: http://math.hws.edu/eck/cs124/javanotes8/c7/s4.html 7.4.3
		for (int i = 1; i < n; i++) {
			for(int j = i; j > 0; j--){
				if(list[j] < list[j-1]){
					int temp = list[j];
					list[j] = list[j-1];
					list[j-1] = temp;
				}
			}
		}
	}

	public static long bubbleSortTime(int n) {
		int[] list = new int[n];
		System.arraycopy(data,0,list,0,n);
		long timeStart = System.currentTimeMillis();
		bubbleSort(n, list);
		long timeEnd = System.currentTimeMillis();
		long timeCost = timeEnd - timeStart;
		return timeCost;
	}

	public static void bubbleSort(int n, int[] list) {
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - i - 1; j++) {
				if (list[j] > list[j + 1]) {
					// swap
					int tmp = list[j + 1];
					list[j + 1] = list[j];
					list[j] = tmp;
				}
			}
		}
	}

	public static long selectionSortTime(int n) {
		int[] list = new int[n];
		System.arraycopy(data,0,list,0,n);
		long timeStart = System.currentTimeMillis();
		selectionSort(n, list);
		long timeEnd = System.currentTimeMillis();
		long timeCost = timeEnd - timeStart;
		return timeCost;
	}

	public static void selectionSort(int n, int[] list) {
		// TODO :add your code here
		// reference: http://math.hws.edu/eck/cs124/javanotes8/c7/s4.html 7.4.4
		for (int i = 0; i < n-1; i++) {
			int minValue = Integer.MAX_VALUE;
			int minIndex = -1;
			for (int j = i; j < n; j++) {
				if(list[j] < minValue){
					minIndex = j;
					minValue = list[j];
				}
			}
			int temp = list[minIndex];
			list[minIndex] = list[i];
			list[i] = temp;
		}
	}

	public static long quickSortTime(int n) {
		int[] list = new int[n];
		System.arraycopy(data,0,list,0,n);
		long timeStart = System.currentTimeMillis();
		quickSort(n, list);
		long timeEnd = System.currentTimeMillis();
		long timeCost = timeEnd - timeStart;
		return timeCost;
	}

	public static void quickSort(int n, int[] list) {
		// TODO :add your code here
		// Adam's ppt
		qs(0, n, list);
	}

	public static void qs(int left, int right, int[] list) {
		if(right - left <= 1)
			return;
		if(right - left == 2){
			if(list[left] > list[left+1]) {
				int temp = list[left+1];
				list[left+1] = list[left];
				list[left] = temp;
			}
			return;
		}

		int x = (int)((Math.random() * (right-left)) + left);
//		int x = left;
		int temp = list[x];
		list[x] = list[left];
		list[left] = temp;

		int i = left, j = right - 1;
		while(i < j){
			for(; i < j; j--)
				if(list[j] < list[left])
					break;
			if(i >= j)	break;
			for(; i < j; i++)
				if(list[i] > list[left])
					break;
			if(i >= j)	break;
			temp = list[i];
			list[i] = list[j];
			list[j] = temp;
		}
		temp = list[i];
		list[i] = list[left];
		list[left] = temp;
		if(left < i-1)
			qs(left, i, list);
		if(right > i+2)
			qs(i+1, right, list);
	}

	public static long mergeSortTime(int n) {
		int[] list = new int[n];
		System.arraycopy(data,0,list,0,n);
		long timeStart = System.currentTimeMillis();
		mergeSort(n, list);
		long timeEnd = System.currentTimeMillis();
		long timeCost = timeEnd - timeStart;
		return timeCost;
	}

	public static void mergeSort(int n, int[] list) {
		// TODO :add your code here
		// https://introcs.cs.princeton.edu/java/42sort/ Mergesort
		list = ms(n, list);
	}

	public static int[] ms(int n, int[] a) {
		if(n > 1) {
			int p = n/2;
			int[] b = new int[p];
			int[] c = new int[n-p];
			for(int i = 0; i < n; i++) {
				if(i < p)
					b[i] = a[i];
				else
					c[i-p] = a[i];
			}
			b = ms(p, b);
			c = ms(n-p, c);
			a = merge(p, b, n-p, c);
		}
		return a;
	}

	public static int[] merge(int n1, int[] l, int n2, int[] r) {
		int n = n1 + n2;
		int[] a = new int[n];
		int i = 0, j = 0;
		for(int k = 0; k < n; k++) {
			if(i < n1 && (j >= n2 || l[i] < r[j])) {
				a[k] = l[i];
				i++;
			}
			else {
				a[k] = r[j];
				j++;
			}
		}
		return a;
	}

	public static long heapSortTime(int n) {
		int[] list = new int[n];
		System.arraycopy(data,0,list,0,n);
		long timeStart = System.currentTimeMillis();
		heapSort(n, list);
		long timeEnd = System.currentTimeMillis();
		long timeCost = timeEnd - timeStart;
		return timeCost;
	}

	public static void heapSort(int n, int[] list) {
		// TODO :add your code here
		// optional

	}

}
