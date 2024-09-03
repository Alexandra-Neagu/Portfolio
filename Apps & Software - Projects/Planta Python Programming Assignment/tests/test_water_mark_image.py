import numpy as np
import pytest

from src.image_processing import water_mark_image


@pytest.fixture
def input_image():
    return np.zeros((500, 500, 3), dtype=np.uint8)


def test_water_mark_image(input_image):
    output_image = water_mark_image(input_image)
    top_left = output_image[:200, :200, :]
    assert not np.array_equal(top_left, np.zeros((200, 200, 3), dtype=np.uint8))
