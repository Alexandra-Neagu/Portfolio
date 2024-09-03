import shutil
from pathlib import Path
import cv2
import os
from numpy.typing import NDArray

from src.image_processing import process_snapshot, read_image, write_image


def process_snapshots():
    snapshots_paths = get_incoming_complete_snapshots()
    for snapshot_path in snapshots_paths:
        process_snapshot(snapshot_path)
        copy_to_uploaded(snapshot_path)


def copy_to_uploaded(snapshot_path: Path):
    for image_name in ['image_a.jpg', 'image_b.jpg']:
        image_path = snapshot_path / image_name
        upload_path = Path('images/upload') / snapshot_path.name / image_name
        upload_path.parent.mkdir(parents=True, exist_ok=True)
        shutil.move(image_path, upload_path)
    shutil.rmtree(snapshot_path)


def get_incoming_complete_snapshots() -> [Path]:
    # Old ver, doesn't work
    # incoming_path = Path('images/incoming')
    # New ver
    incoming_path = Path(__file__).resolve().parent.parent / "images" / "incoming"
    snapshot_paths = []
    for snapshot_folder in incoming_path.iterdir():
        # Extension, check the validity of the snapshot
        if snapshot_has_valid_size(snapshot_folder) and not snapshot_has_other_files(
                snapshot_folder) and not snapshot_has_corrupt_image(snapshot_folder):
            if (snapshot_folder / 'image_a.jpg').exists() and (snapshot_folder / 'image_b.jpg').exists():
                snapshot_paths.append(snapshot_folder)
    return snapshot_paths


# Alexandra's code

def snapshot_has_valid_size(snapshot_folder: Path, size: int = 2) -> bool:
    # Count the number of image files in the snapshot folder
    image_count = sum(1 for file in snapshot_folder.iterdir() if
                      file.is_file() and file.suffix.lower() in ['.jpg', '.jpeg',
                                                                 '.png'])  # Added more image extensions for the future
    # Check if the snapshot folder contains exactly the required number of image files
    return image_count == size


def snapshot_has_corrupt_image(snapshot_folder: Path) -> bool:
    # Iterate through all files in the snapshot folder
    for file_path in snapshot_folder.iterdir():
        # Check if the file is a regular file
        if file_path.is_file():
            # Check if the file is an image
            if file_path.suffix.lower() in ['.jpg', '.jpeg', '.png']:
                # Attempt to read the image
                image = cv2.imread(str(file_path))
                # Check if the image is corrupt (so unable to be read)
                if image is None:
                    return True  # Found a corrupt image
    return False  # No corrupt images found


# Bonus feature
def snapshot_has_other_files(snapshot_folder: Path) -> bool:
    # Iterate through all files in the snapshot folder
    for file_path in snapshot_folder.iterdir():
        # Check if the file is a regular file
        if file_path.is_file():
            # Check if the file is not an image
            if file_path.suffix.lower() not in ['.jpg', '.jpeg', '.png']:
                return True  # Found non-image file
    return False  # Snapshot contains only images


def write_compressed_image(image: NDArray, image_path: Path, quality: int = 95):
    # Define the compression parameters
    compression_params = [cv2.IMWRITE_JPEG_QUALITY, quality]
    cv2.imwrite(str(image_path), image, compression_params)


# Alternate way of compressing, after the image is written
def compress_image(image: NDArray, quality: int = 95) -> NDArray:
    # Define the compression parameters
    encode_param = [cv2.IMWRITE_JPEG_QUALITY, quality]
    _, encoded_image = cv2.imencode('.jpg', image, encode_param)
    # Decode the encoded image
    return cv2.imdecode(encoded_image, cv2.IMREAD_COLOR)


def compress_images_in_directory(output_directory: Path, quality: int = 95):
    # Create the output directory if it doesn't exist
    output_directory.mkdir(parents=True, exist_ok=True)

    # Iterate through all snapshot folders in the input directory
    for snapshot_folder in get_incoming_complete_snapshots():
        if not snapshot_folder.is_dir():
            continue  # Skip if not a directory

        # Create corresponding snapshot folder in the output directory
        output_snapshot_folder = output_directory / snapshot_folder.name
        output_snapshot_folder.mkdir(parents=True, exist_ok=True)
        # Iterate through all files in the snapshot folder
        for file_name in os.listdir(snapshot_folder):
            file_path = snapshot_folder / file_name

            # Check if the file is a regular file
            if file_path.is_file():
                # Check if the file is an image
                if file_path.suffix.lower() in ['.jpg', '.jpeg', '.png']:  # Added more image extensions for the future
                    image = read_image(file_path)
                    # Write the compressed image to the output snapshot folder
                    output_file_path = output_snapshot_folder / file_name
                    write_compressed_image(image, output_file_path, quality)


def downscale_image(image: NDArray, target_size: tuple = (500, 500)) -> NDArray:
    return cv2.resize(image, target_size, interpolation=cv2.INTER_AREA)


def downscale_images_in_directory(output_directory: Path, target_size: tuple = (500, 500)):
    # Create the output directory if it doesn't exist
    output_directory.mkdir(parents=True, exist_ok=True)

    # Iterate through all snapshot folders in the input directory
    for snapshot_folder in get_incoming_complete_snapshots():
        if not snapshot_folder.is_dir():
            continue  # Skip if not a directory

        # Create corresponding snapshot folder in the output directory
        output_snapshot_folder = output_directory / snapshot_folder.name
        output_snapshot_folder.mkdir(parents=True, exist_ok=True)

        # Iterate through all files in the snapshot folder
        for file_name in os.listdir(snapshot_folder):
            file_path = snapshot_folder / file_name

            # Check if the file is a regular file
            if file_path.is_file():
                # Check if the file is an image
                if file_path.suffix.lower() in ['.jpg', '.jpeg', '.png']:  # Added more image extensions for the future
                    image = read_image(file_path)
                    # Downscale the image
                    downscaled_image = downscale_image(image, target_size)
                    # Write the downscaled image to the output directory
                    output_file_path = output_snapshot_folder / file_name
                    write_image(downscaled_image, output_file_path)


def rotate_image(image: NDArray) -> NDArray:
    # Rotate the image by 180 degrees
    return cv2.rotate(image, cv2.ROTATE_180)


def rotate_images_in_directory(output_directory: Path, filename: str):
    # Create the output directory if it doesn't exist
    output_directory.mkdir(parents=True, exist_ok=True)

    # Iterate through all snapshot folders in the input directory
    for snapshot_folder in get_incoming_complete_snapshots():
        if not snapshot_folder.is_dir():
            continue  # Skip if not a directory

        # Create corresponding snapshot folder in the output directory
        output_snapshot_folder = output_directory / snapshot_folder.name
        output_snapshot_folder.mkdir(parents=True, exist_ok=True)

        # Find the specified file in the snapshot folder
        file_path = snapshot_folder / filename

        if file_path.exists():  # Check if the specified file exists
            if file_path.is_file():  # Check if the file is a regular file
                if file_path.suffix.lower() in ['.jpg', '.jpeg', '.png']:  # Added more image extensions for the future
                    image = read_image(file_path)

                    # Rotate the image
                    rotated_image = rotate_image(image)

                    # Write the rotated image to the output snapshot folder
                    output_file_path = output_snapshot_folder / filename
                    write_image(rotated_image, output_file_path)


def processing_pipeline(image: NDArray, file_name: str, rotate_file: str, compression_quality: int = 95,
                        downscale_size: tuple = (500, 500)) -> NDArray:
    # Step 1: Compress the image
    image = compress_image(image, compression_quality)
    # Step 2: Downscale the image
    image = downscale_image(image, downscale_size)
    # Step 3: Rotate the image if it's supposed to be rotated
    if file_name == rotate_file:
        image = rotate_image(image)

    # More steps can be added here as the processing pipeline gets extended in the future

    return image


def process_images(output_directory: Path, rotate_file: str, compression_quality: int = 95,
                   downscale_size: tuple = (500, 500)):
    # Create the output directory if it doesn't exist
    output_directory.mkdir(parents=True, exist_ok=True)

    # Iterate through all snapshot folders in the input directory
    for snapshot_folder in get_incoming_complete_snapshots():
        if not snapshot_folder.is_dir():
            continue  # Skip if not a directory

        # Create corresponding snapshot folder in the output directory
        output_snapshot_folder = output_directory / snapshot_folder.name
        output_snapshot_folder.mkdir(parents=True, exist_ok=True)

        # Iterate through all files in the snapshot folder
        for file_name in os.listdir(snapshot_folder):
            file_path = snapshot_folder / file_name

            # Check if the file is a regular file
            if file_path.is_file():
                # Check if the file is an image
                if file_path.suffix.lower() in ['.jpg', '.jpeg', '.png']:  # Added more image extensions for the future
                    image = read_image(file_path)
                    image = processing_pipeline(image, file_name, rotate_file, compression_quality, downscale_size)
                    # Write the rotated image to the output snapshot folder
                    output_file_path = output_snapshot_folder / file_name
                    write_image(image, output_file_path)


if __name__ == '__main__':
    # Get root directory of the project
    root_directory = Path(__file__).resolve().parent.parent
    # Get input and output directories for the processing of the images
    input_directory = root_directory / "images" / "incoming"
    output_directory = root_directory / "images" / "upload"

    # Test compressing an image
    # Construct the path to the image
    # image_path = root_directory / "images" / "incoming" / "snapshot_1" / "image_a.jpg"
    # print(read_image(image_path))
    # print("-------")
    # print(compress_image(read_image(image_path)))

    # Test Version 1 - compressing, then downsizing, then rotating (when applicable) all images
    # Compression quality put very low even though the assignment said "configurable quality" to be able to see a noticeable difference in the compression
    # compress_images_in_directory(input_directory, output_directory, 5)
    # downscale_images_in_directory(output_directory, output_directory, target_size=(500, 500))
    # rotate_images_in_directory(output_directory, output_directory, filename='image_b.jpg')

    # Test Version 2 - processing all images
    # Compression quality put very low even though the assignment said "configurable quality" to be able to see a noticeable difference in the compression
    # process_images(output_directory, rotate_file="image_b.jpg", compression_quality=5, downscale_size=(500, 500))
