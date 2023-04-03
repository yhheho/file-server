import argparse
import os
import requests

DEFAULT_SERVER_ADDRESS = "http://localhost"
DEFAULT_SERVER_PORT = 8080

def store(replace, file_path, name, server_url):
    server_address = get_server_address() if not server_url else server_url
    file_name = os.path.basename(file_path) if not name else name

    try:
        with open(file_path, 'rb') as opened_file:
            file = {'file': ('{}'.format(file_name), open(file_path, 'rb'))}

            if replace:
                url = "{}/files/{}?replace=true".format(server_address, file_name)
            else:
                url = "{}/files/{}".format(server_address, file_name)

            response = requests.post(url, files=file)
            if response.status_code == 200:
                print(file_name + " uploaded.")
            elif response.status_code == 400:
                print("Failed to upload file. Error: {}".format(response.json()['message']))
                error_code = response.json()['errorCode']
                if error_code == "ERR001":
                    show_replace_prompt(file_path, name, server_url)
                    
    except FileNotFoundError:
        print("The file: {} can not be found.".format(file_path))


def delete(name, server_url):
    server_address = get_server_address() if not server_url else server_url
    url = "{}/files/{}".format(server_address, name)
    response = requests.delete(url)
    if response.status_code == 200:
        print(f"Deleting file with name '{name}'.")
    elif response.status_code == 500:
        print("Failed to delete file '{}'. Error: {}".format(name, response.json()['message']))

def list_files(server_url):
    server_address = get_server_address() if not server_url else server_url
    url = "{}/files".format(server_address)
    response = requests.get(url)
    print(response.text)

def get_server_address():
    return "{}:{}".format(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT)

def show_replace_prompt(file_path, name, server_url):
    replace_file = input("Do you want to replace the file? [Y/N] ")
    if replace_file.lower() == "y" or replace_file.lower() == "yes":
        store(True, file_path, name, server_url)
        
def add_store_parser(subparsers):
    store_parser = subparsers.add_parser('fs-store', help='Store a file')
    store_parser.add_argument('--file-path', '-f', help='Path of the file', required=True)
    store_parser.add_argument('--name', '-n', help='Name of the file', required=False)
    store_parser.add_argument('--replace', '-r', help='Replace existing file', action='store_true')
    store_parser.add_argument('--server-url', '-u', help='Url of the server, default is: http://localhost:8080', required=False)

def add_delete_parser(subparsers):
    delete_parser = subparsers.add_parser('fs-delete', help='Delete a file')
    delete_parser.add_argument('--name', '-n', help='Name of the file', required=True)
    delete_parser.add_argument('--server-url', '-u', help='Url of the server, default is: http://localhost:8080', required=False)

def add_list_parser(subparsers):
    list_parser = subparsers.add_parser('fs-list', help='List stored files')
    list_parser.add_argument('--server-url', '-u', help='Url of the server, default is: http://localhost:8080', required=False)


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    subparsers = parser.add_subparsers(title='subcommands', dest='subcommand')

    # add command here
    add_store_parser(subparsers)
    add_delete_parser(subparsers)
    add_list_parser(subparsers)

    args = parser.parse_args()
    if args.subcommand == 'fs-store':
        store(args.replace, args.file_path, args.name, args.server_url)
    elif args.subcommand == 'fs-delete':
        delete(args.name, args.server_url)
    elif args.subcommand == 'fs-list':
        list_files(args.server_url)
    else:
        parser.print_help()
