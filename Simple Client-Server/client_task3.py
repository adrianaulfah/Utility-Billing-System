import socket
import threading
import sys

SERVER_IP = "127.0.0.1"   
SERVER_PORT = 5002

def receive_messages(sock):
    while True:
        try:
            data = sock.recv(1024).decode('utf-8')
            if not data:
                print("[INFO] Disconnected from server.")
                break
            print(data, end="")  
        except:
            print("[ERROR] Connection lost.")
            break

    sock.close()
    sys.exit(0)

def main():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.connect((SERVER_IP, SERVER_PORT))
    print("[CONNECTED] You can start chatting. Type /quit to exit.")

    
    recv_thread = threading.Thread(target=receive_messages, args=(sock,))
    recv_thread.daemon = True
    recv_thread.start()

    
    while True:
        msg = input("")
        if msg.strip().lower() == "/quit":
            break
        try:
            sock.send((msg + "\n").encode('utf-8'))
        except:
            print("[ERROR] Failed to send. Exiting.")
            break

    sock.close()

if __name__ == "__main__":
    main()

