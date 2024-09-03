from pathlib import Path

import cv2
from numpy.typing import NDArray

def read_image(image_path: Path) -> NDArray:
    return cv2.imread(str(image_path), cv2.IMREAD_COLOR)


def write_image(image: NDArray, image_path: Path):
    cv2.imwrite(str(image_path), image)


def water_mark_image(image: NDArray) -> NDArray:
    logo = cv2.imread(str(Path('logo/plant_logo.png')))
    if image is None:
        return logo
    image[:200, :200, :] = logo
    return image


def process_snapshot(snapshot_path: Path):
    image_a = read_image(snapshot_path / 'image_a.jpg')
    image_a = water_mark_image(image_a)
    write_image(image_a, snapshot_path / 'image_a.jpg')



