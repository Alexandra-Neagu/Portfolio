import json
import os
from mastodon import Mastodon, StreamListener
from datetime import datetime, timedelta


class StreamTimeoutError(Exception):
    pass


class DateTimeEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime):
            return obj.isoformat()
        return super().default(obj)


# Create an app and obtain the client credentials.
# Substitute the name of your app and the name of the credentials file.
Mastodon.create_app("WSE_Task1",
                    api_base_url="https://mastodon.social",
                    to_file="your_mastodon_file.secret" # Substitute with own secret file
                    )
# Substitute the name of the credentials file.
mastodon = Mastodon(
    client_id="your_mastodon_file.secret" # Substitute with own secret file
)
# Log in with your account.
# Substitute your e-mail and password and the name of the credentials file.
mastodon.log_in("your_email@gmail.com", "your_password", # Substitute with own login details
                to_file="your_mastodon_file.secret" # Substitute with own secret file
                )


# Class for sub-task 1.2
class StdOutListener(StreamListener):
    """
    A listener handles Statuses received from the stream.
    This basic listener simply prints received statuses to stdout.
    It also saves them to results.json
    """

    def __init__(self, max_duration_minutes=10):
        super().__init__()
        self.start_time = None
        self.max_duration = timedelta(minutes=max_duration_minutes)  # Duration of monitoring
        self.first_status_id = None
        self.last_status_id = None
        self.status_count = 0
        self.results = []  # To store statuses for later serialization

    def on_update(self, status):
        if not self.start_time:
            self.start_time = datetime.now()

        if not self.first_status_id:
            self.first_status_id = status['id']

        # Prints the received status to the console
        print(status)

        self.status_count += 1
        self.results.append(status)  # Store the status for later serialization

        # Check if max duration has passed
        elapsed_time = datetime.now() - self.start_time
        if elapsed_time > self.max_duration:
            self.last_status_id = status['id']

            print()  # Extra line for better readability in the console
            print(f"Start Time: {self.start_time}")
            print()

            print(f"First Status ID: {self.first_status_id}")
            print()

            print(f"Last Status ID: {self.last_status_id}")
            print()

            print(f"End Time: {datetime.now()}")
            print()

            print(f"Total Statuses: {self.status_count}")
            print()

            # Serialize results to a JSON file using the custom encoder for datetime
            with open("results.json", "w", encoding="utf-8") as json_file:
                json.dump(self.results, json_file, ensure_ascii=False, indent=4, cls=DateTimeEncoder)

            # Stop monitoring the stream
            raise StreamTimeoutError("Stream duration exceeded")

    def on_abort(self, err):
        print(err)
        print()
        raise StreamTimeoutError("Error in streaming")


# Class for sub-task 1.3
class IsraelPalestineListener(StreamListener):
    """
    A listener handles statuses received from the stream.
    This listener filters statuses related to the Israel-Palestine Conflict.
    """

    # Set the max duration to 120 mins because when set as 2h it was giving a session connection error
    def __init__(self, max_duration_minutes=120):
        super().__init__()
        self.start_time = None
        self.max_duration = timedelta(minutes=max_duration_minutes)  # Duration of monitoring
        self.status_count = 0
        self.related_status_count = 0

    def on_update(self, status):
        if not self.start_time:
            self.start_time = datetime.now()

        self.status_count += 1

        # Extract text from status
        status_text = status.get('content', '')

        # List of keywords for filtering
        keywords = ['israel', 'palestine', 'hamas', 'gaza', 'israel-palestine conflict']

        # Check if any keyword is present in the status text
        if any(keyword in status_text.lower() for keyword in keywords):
            self.related_status_count += 1

            # If the status is related to the Israel-Palestine Conflict, print it to the console
            # (also helps for debuting purposes)
            print(status)

        # Check if max duration has passed
        elapsed_time = datetime.now() - self.start_time
        if elapsed_time > self.max_duration:
            print()  # Extra line for better readability in the console
            print(f"Start Time: {self.start_time}")
            print()

            print(f"End Time: {datetime.now()}")
            print()

            print(f"Total Statuses: {self.status_count}")
            print()

            print(f"Related Statuses: {self.related_status_count}")
            print()

            # Stop monitoring the stream
            raise StreamTimeoutError("Stream duration exceeded")

    def on_abort(self, err):
        print(err)
        print()
        raise StreamTimeoutError("Error in streaming")


# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    # Code related to sub-task 1.2. Uncomment to run it, and comment out the code for sub-task 1.3
    '''
    l1 = StdOutListener()
    try:
        mastodon.stream_public(listener=l1)
    except StreamTimeoutError:
        pass  # Ignore the exception as it indicates the end of streaming
    
    # Check the size of the results file
    file_size = os.path.getsize("results.json")
    print(f"Size of the results file: {file_size} bytes")
    '''

    # Code related to sub-task 1.3
    l2 = IsraelPalestineListener()
    try:
        mastodon.stream_public(listener=l2)
    except StreamTimeoutError:
        pass  # Ignore the exception as it indicates the end of streaming
