package defaultpackage;

import java.net.SocketException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import keyboardinput.Keyboard;


public class MainTest {

	/**
	 * @param args
	 */
	private ObjectOutputStream out;

	private ObjectInputStream in ; // stream con richieste del client

    private List<String> fileList= new ArrayList<>();

    /**
     * Costruttore della classe MainTest.
     * @param ip, indica l'indirizzo del server.
     * @param port, porta alla quale si connette il client.
     * @throws IOException
     */
	public MainTest(String ip, int port) throws IOException{
		InetAddress addr = InetAddress.getByName(ip); //ip
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, port); //Port
		System.out.println(socket);
		
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());	 // stream con richieste del client
	}

    /**
     * Mostra il menu dal quale l'utente può scegliere cosa fare inserendo da tastiera il numero.
     * @return la risposta dell'utente.
     */
	
	private int menu(){
		int answer;
		
		do{
			System.out.println("(1) Load clusters from file");
			System.out.println("(2) Load data from db");
			System.out.print("(1/2):");
			answer=Keyboard.readInt();
		}
		while(answer<=0 || answer>2);
		return answer;
		
	}

    /**
     * Quando l'utente dà come risposta 1, viene chiesto che file si vuole caricare e invia la risposta al server.
     * Aspetta che il server gli invii l'OK e poi legge il contenuto del file.
     * @return il contentuo file selezionato.
     * @throws SocketException
     * @throws ServerException
     * @throws IOException
     * @throws ClassNotFoundException
     */
	
	private String learningFromFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(3);
        do{
            System.out.println("Enter file name:");
            String fileName = Keyboard.readString();
            if(fileList.contains(fileName)){
                out.writeObject(fileName);
                String result = (String)in.readObject();
                if(result.equals("OK"))
                    return (String)in.readObject();
                else throw new ServerException(result);
            }else {
                System.out.println("File not found.");

            }
        }while(true);

    }

    /**
     * Chiede all'utente da quale tabella del Database prendere i dati inoltrando la risposta al server. Se il client
     * non riceve l'OK solleva l'eccezione.
     * @throws SocketException
     * @throws ServerException
     * @throws IOException
     * @throws ClassNotFoundException
     */

    private void storeTableFromDb() throws SocketException,ServerException,IOException,ClassNotFoundException{
        out.writeObject(0);
        String tabName;
        do{
            System.out.print("Table name:");
            tabName = Keyboard.readString();
        }while(!tabName.equals("playtennis"));
        out.writeObject(tabName);
        String result = (String)in.readObject();
        if(!result.equals("OK"))
            throw new ServerException(result);

    }

    /**
     * Chiede all'utente di inserire il raggio e manda la risposta al server. Aspetta di ricevere l'OK dal server per
     * stampare il numero di cluster e i centroidi con le tuple.
     * @return centroidi più popolosi con le tuple.
     * @throws SocketException
     * @throws ServerException
     * @throws IOException
     * @throws ClassNotFoundException
     */
	private String learningFromDbTable() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(1);
		double r=1.0;
		do{
			System.out.print("Radius:");
			r=Keyboard.readDouble();
		} while(r<=0);
		out.writeObject(r);
		String result = (String)in.readObject();
		if(result.equals("OK")){
            System.out.println("Number of Clusters:"+in.readObject());
			return (String)in.readObject();
		}
		else throw new ServerException(result);
		
		
	}

    /**
     * Richiede all'utente di scrivere il nome con il quale salvare il file. Dopo aver informato il server,
     * l'utente riceve la conferma che i cluster sono stati salvati nel file in formato dmp.
     * Se il client non riceve l'OK solleva l'eccezione.
     * @throws SocketException
     * @throws ServerException
     * @throws IOException
     * @throws ClassNotFoundException
     */
	
	private void storeClusterInFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(2);
        System.out.println("Backup file name:");
        String file = Keyboard.readString();
        fileList.add(file);
		out.writeObject(file);
        System.out.println("\nSaving clusters in " + file +
                ".dmp\nSaving transaction ended!\n");
		String result = (String)in.readObject();
		if(!result.equals("OK"))

            throw new ServerException(result);
	}

    /**
     * In base alla risposta dell'utente si divide in due casi nei quali sfrutta i metodi precedentemente descritti.
     * Al termine richiede se si vuole effettuare una nuova esecuzione.
     * @param args
     */
	public static void main(String[] args) {
		String ip=args[0];
		int port = Integer.parseInt(args[1]);
		MainTest main=null;
		try{
			main=new MainTest(ip,port);
		}
		catch (IOException e){
			System.out.println(e);
			return;
		}
		
		
		do{
            //legge la risposta dell'utente ottenuta da menu.
			int menuAnswer=main.menu();
			switch(menuAnswer)
			{
				case 1:
					try {
                        //usa il metodo indicato per poi stampare a video il contenuto del file
						String kmeans=main.learningFromFile();
						System.out.println(kmeans);
					}
					catch (SocketException e) {
						System.out.println(e);
						return;
					}
					catch (FileNotFoundException e) {
						System.out.println(e);
						return ;
					} catch (IOException e) {
						System.out.println(e);
						return;
					} catch (ClassNotFoundException e) {
						System.out.println(e);
						return;
					}catch (ServerException e) {
                        System.out.println(e);
                        return;
                    }
                    break;
				case 2: // learning from db
				
					while(true) {
                        try {
                            //ottiene il nome della tabella
                            main.storeTableFromDb();
                            break; //esce fuori dal while
                        } catch (SocketException e) {
                            System.out.println(e);
                            return;
                        }catch(ServerException e){
                                System.out.println(e);
                                return;
                        } catch (FileNotFoundException e) {
                            System.out.println(e);
                            return;

                        } catch (IOException e) {
							System.out.println(e);
							return;
						} catch (ClassNotFoundException e) {
							System.out.println(e);
							return;
						}
                    } //end while [viene fuori dal while con un db (in alternativa il programma termina)
						
					char answer='y';//itera per learning al variare di k
					do{
						try
						{
                            //richiede il raggio e calcola il cluster più popoloso
							String clusterSet=main.learningFromDbTable();
							System.out.println(clusterSet);
                            if(!clusterSet.isEmpty())
							    //successivamente lo salva in un file
							    main.storeClusterInFile();

									
						}
						catch (SocketException e) {
							System.out.println(e);
							return;
						}catch(ServerException e){
                            System.out.println(e);
                            return;
                        }
						catch (FileNotFoundException e) {
							System.out.println(e);
							return;
						}
						catch (ClassNotFoundException e) {
							System.out.println(e);
							return;

						}catch (IOException e) {
							System.out.println(e);
							return;
						}

                        do{
                            System.out.print("Would you repeat?(y/n)");
                            answer=Keyboard.readChar();
                        }while(Character.toLowerCase(answer)!='y' && Character.toLowerCase(answer)!='n');


					} while(Character.toLowerCase(answer)=='y');
                    break;
			}
            char answer;
			do{
                System.out.print("would you choose a new operation from menu?(y/n)");
                answer = Keyboard.readChar();
            } while(Character.toLowerCase(answer)!='y' && Character.toLowerCase(answer)!='n');
            if(answer != 'y')
                break;
        } while(true);
		}
	}



