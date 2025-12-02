import socket
import threading

HOST = "0.0.0.0"
PORT = 5002

clients = []  

def broadcast(message, source_conn):
    """Hantar message ke semua client kecuali sender."""
    for client_conn, _ in clients:
        if client_conn != source_conn:
            try:
                client_conn.send(message)
            except:
                client_conn.close()
                clients.remove((client_conn, _))

def handle_client(conn, addr):
    print(f"[NEW CONNECTION] {addr} connected.")
    conn.send("Welcome to the chat room!\n".encode('utf-8'))

    while True:
        try:
            msg = conn.recv(1024)
            if not msg:
                break  
            text = msg.decode('utf-8')
            print(f"[{addr}] {text.strip()}")

            
            full_msg = f"[{addr[0]}:{addr[1]}] {text}".encode('utf-8')
            broadcast(full_msg, conn)
        except:
            break

    
    print(f"[DISCONNECTED] {addr}")
    conn.close()
    for c in clients:
        if c[0] == conn:
            clients.remove(c)
            break

def main():
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind((HOST, PORT))
    server_socket.listen(5)
    print(f"[LISTENING] Server is running on {HOST}:{PORT}")

    while True:
        conn, addr = server_socket.accept()
        clients.append((conn, addr))

        # buat thread untuk client ni
        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.daemon = True
        thread.start()

        print(f"[ACTIVE CONNECTIONS] {len(clients)}")

if __name__ == "__main__":
    main()

