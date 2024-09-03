from time import sleep

from src.data_handling import process_snapshots


def system_loop():
    while True:
        print("Processing snapshots")
        process_snapshots()
        print("Sleeping for 5 seconds")
        sleep(5)


if __name__ == '__main__':
    system_loop()
