# ASCII Art Converter in JAVA
This project implements an ASCII Art Converter in Java that transforms image files into ASCII character representations.
It was developed as part of OOP course at The Hebrew University of Jerusalem.

# Project Structure
- ascii_art package: Contains the core logic for user input, the main algorithm, and program flow.
  - AsciiArtAlgorithm.java – Core logic for converting an image to ASCII art.
  - KeyboardInput.java – Pre-Implemented -Handles user input via the keyboard.
  - Shell.java – Command-line interface (CLI) for running the program.
  - Exceptions/ – Custom exception classes used in the project.

- ascii_output package: Pre-Implemented - Responsible for generating the final ASCII output.
  - AsciiOutput.java – Base interface for ASCII output.
  - ConsoleAsciiOutput.java – Prints ASCII output to the console.
  - HtmlAsciiOutput.java – Generates HTML output for viewing in a browser.

- image package: Handles image processing such as loading, resizing, dividing, and more.
  - DividedImage.java – Divides an image into blocks.
  - Image.java – Represents a grayscale image.
  - ImageBrightness.java – Calculates pixel brightness.
  - PaddingImage.java – Handles padding of images for uniform division.

- image_char_matching package: Contains logic for matching image blocks to ASCII characters based on brightness.
  - CharConverter.java – Converts brightness values to ASCII characters.
  - SubImgCharMatcher.java – Matches image blocks to best-fitting ASCII characters.
