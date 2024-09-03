Use this file to explain your reasoning and the process you followed in the project.

DISCLAIMER: In the assignment it says to "to implement within the data handling process", but then mentions "Add the following image processing steps".
I was unsure to where to add my code, _data_handling.py_ or _image_processing.py_, but opted for _data_handling.py_ in the end. However, the code can be swapped seamlessly interchangebly inside the other class.

DISCLAIMER 2: I have added comments all throughout my methods. I also thought about adding docstrings to the functions, but decided against it. I believe my comments and my chosen naming convension make the functionality clear enough, and I feel that the docstring would've just cluttered the view more and counter-intuitively made the code more unreadable.

What I have implemented in _data_handling.py_:

- Modified get_incoming_complete_snapshots(). The Path was throwing an error on my machine, so I fixed it. I also extended it to not only check if a snapshot contains image_a and image_b jpgs, but also to check if the a snapshot has exactly 2 files (in case in the future a snapshot would potentially have more/fewer files), if all files are images and not other types, and if none of the said images are corrupt. If all these conditions are satisfied, a snapshot is considered valid and added to the paths.
- snapshot_has_valid_size() checks if the specified snapshot has valid size. You can pass its size in a param, by default is 2
- snapshot_has_corrupt_image() checks if the specified snapshot has any corrupt images in it. 
- snapshot_has_other_files(), bonus feature, checks if the specified snapshot contains any files other than image files. Also, throughout the assignment, when checking for images in the snapshots, I extended to accept jpg, jpeg, and png files. In the future, also consider adding other types (e.g. .tiff) so that a snapshot wouldn't accidentaly be labelled as invalid even though it contains image files.
- write_compressed_image() alternative to write_image() in _image_processing.py_, compresses the image before writing it. Could be moved to _image_processing.py_
- compress_image() alternative to write_compressed_image(), compresses an already written image

Now, for the processing steps, I decided to not use copy_to_uploaded(), because it was deleting the incoming snapshots when copying them to upload. I wanted to still keep them such that I would test the functionality and play around with it to make sure it works as intended. Also, it was hardcoding path and file names, and I wanted from the start to not do that. Therefore, to output images in the upload folder, I either implemented my own functionality (e.g. write_compressed_image()) or used the ones from _image_processing.py_ (e.g., write_image()). However, my functions could be easily integrated with copy_to_uploaded().
Also, I made 2 versions of the processing step, each with their own advantages and disadvantages. As I don't know your architecture, amount of data, computational constraints, etc., I opted to implement both.

- compress_image() compresses the specified image with the specified quality (default 95)
- downscale_image() downscales the specified image with the specified size (default (500, 500))
- rotate_image() rotates the specified image 180 degrees

Version 1:

- You can compress/downsize/rotate all images in the the valid snapshots of the specified directory separate from the other steps. So it does one step of the processing to all the images it CAN (e.g. it doesn't rotate invalid images), and then writes them to the corresponding folder in upload
- compress_images_in_directory() self-explanatory, uses write_compressed_image() for efficiency
- downscale_images_in_directory() self-explanatory, uses downscale_image()
- rotate_images_in_directory() self-explanatory, uses rotate_image() and only rotates images matching the desired filename passed as a param
- Basically, this version keeps track of the intermediate results. It first compresses all valid images, and writes them. Then it downsizes all valid images, and writes them. It helps with visualising and debugging the effects of a processing step.

Version 2: 

- It processes each image through each step in one pipeline, and then puts the result in upload.
- So it takes each image through each step, and at only at the end of the processing pipeline writes the result to upload.
- processing_pipeline() does the processing steps outlined in the assignment. It can easily be extended with more steps in the future.
- process_images() processes all the valid images through the pipeline, uses processing_pipeline()

Due to lack of time because of my choice to focus on refining the existing features, providing bonus ones, and providing multiple design choices based on the current architecture's implementation (and of writing on this file a bit too much, but I wanted to extensive :)), I didn't get to extend the unit test suite. 
However, I provided debugging tests in the _main_ part in _data_handling.py_, accompanied by explanatory comments. I provided tests for both versions of the processing step. To run them and check that the code works, just uncomment said lines (Version 1 - lines 247-249, Version 2 - line 253).

Lastly, I also did not get to implement the first bullet point of Bonuses, because I was also unsure what summary of states would make sense for the app to know. Just at first thought, I could summarize how many raw states are valid, at what steps of the processing they are, for the processed states what steps they went throuh/how the processing looked like. Due to the how I designed my code, this could have been easily done.