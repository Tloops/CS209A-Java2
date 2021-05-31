# CP

## Reflection

### Generic

```java
public class MyClass {
    public List<String> stringList = ...;
}

    Field field = MyClass.class.getField("stringList");
    Type genericFieldType = field.getGenericType();
    if(genericFieldType instanceof ParameterizedType){
        ParameterizedType aType = (ParameterizedType) genericFieldType;
        Type[] fieldArgTypes = aType.getActualTypeArguments();
        for(Type fieldArgType : fieldArgTypes){
            Class fieldArgClass = (Class) fieldArgType;
            System.out.println("fieldArgClass = " + fieldArgClass);
        }
    }
```

### Array

```java
int[] intArray = (int[]) Array.newInstance(int.class, 3);
Array.set(intArray, 0, 123);
Array.set(intArray, 1, 456);
Array.set(intArray, 2, 789);
System.out.println("intArray[0] = " + Array.get(intArray, 0));
System.out.println("intArray[1] = " + Array.get(intArray, 1));
System.out.println("intArray[2] = " + Array.get(intArray, 2));
```



## socket

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public class ServerDemo {

	public static void main(String[] args) throws IOException {
		ArrayList<Integer> data = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			data.add(i);
		}

		HashMap<String, Function<ArrayList<Integer>, ArrayList<Integer>>> serviceProcess = new HashMap<>();
		serviceProcess.put("GetEven", (l) -> {
			l.removeIf((e) -> e % 2 != 0);
			return l;
		});
		serviceProcess.put("GetOdd", (l) -> {
			l.removeIf((e) -> e % 2 == 0);
			return l;
		});
		int portNumber = 8888;
		PrintWriter out = null;
		BufferedReader in = null;
		ServerSocket serverSocket = null;
		Socket clientSocket = null;

		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server's OK, waiting for connect...");
			while (true) {
				clientSocket = serverSocket.accept();
				System.out.println("Hava a connect");

				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String inputLine, outputLine;
				// Wait for input
				if ((inputLine = in.readLine()) != null) {
					String[] command = inputLine.split(" ");
					String response = "";
					if (serviceProcess.containsKey(command[0])) {
						System.out.println(command[0]);
						ArrayList<Integer> newl = serviceProcess.get(command[0]).apply(data);
						response = newl.toString();
					} else {
						response = "Goodbye";
					}
					out.println(response);
				}
				clientSocket.close();
			}
		} catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			if (clientSocket != null) {
				clientSocket.close();
			}
			if (serverSocket != null) {
				serverSocket.close();
			}
		}
	}
}
```

```java
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientDemo {

	public static void main(String[] args) {
		boolean loop = true;
		String hostName = "localhost";
		int portNumber = 8888;
		while (loop) {
			try (Socket sock = new Socket(hostName, portNumber);
					//set autoFlush true
					PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));) {
				System.out.println("Please send a command:");
				Scanner sc = new Scanner(System.in);
				// read from input
				String info = sc.nextLine();
				// send to server
				out.println(info);
				// read from server
				String fromServer;
				while ((fromServer = in.readLine()) != null) {
					if (fromServer.length() > 0) {
						System.out.println(fromServer);
					}
					if (fromServer.equals("Goodbye")) {
						loop = false;
						break;
					}
				}
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host " + hostName);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);
			}
		}
	}
}
```

## multi tasking

```java
class Mythread extends Thread{
    @Override
    public void run() {
        for (int i = 0; i < 50 ; i++) {
            if (i % 2 == 0){
                System.out.println(Thread.currentThread().getName() + ":"+i);
            }
        }
    }
}

public class ThreadTest {

    public static void main(String[] args) {
        Mythread mythread = new Mythread();
        mythread.start();
        
        // 这个仍然是在main线程中执行
        for (int i = 0; i < 50 ; i++)
            if (i % 2 != 0)
                System.out.println(Thread.currentThread().getName() + ":"+i);
    }
}
/*
注意：
1. 如果自己手动调用run()方法，那么就只是普通方法，没有启动多线程模式。
2. run()方法由JVM调用，什么时候调用，执行的过程控制都有操作系统的CPU调度决定。
3. 想要启动多线程，必须调用start方法。
4. 一个线程对象只能调用一次start()方法启动，如果重复调用了，则将抛出以上 的异常“IllegalThreadStateException”
*/
// 也可以使用匿名内部类简单的写
new Thread(){
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0)
                System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}.start();
```

## Serialize

```java
import java.io.*;

public class Person implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + "]";
    }

    public static void main(String[] args) {
        Person p = new Person();
        p.setName("feige");
        writeObj(p);
        Person p2 = readObj();
        System.out.println(p2);
    }

    // 序列化
    public static void writeObj(Person p) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("1.txt"));
            objectOutputStream.writeObject(p);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 反序列化
    public static Person readObj() {
        Person p = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("1.txt"));
            try {
                p = (Person)objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }
}
```

