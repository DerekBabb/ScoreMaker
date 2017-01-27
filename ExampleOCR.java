public class ExampleOCR implements DigitReader
{
  PictureEdit digitPic; 
  
  public void setImage(PictureEdit pic)
  {
    digitPic = pic;  //load the the input image into the digitPic field.
  }
  
  public int getDigit()
  {
    //This is the method you should process the image.
    
    return (int)(Math.random() * 10);
  }
  
  public String getName()
  {
    return "Example OCR";
  }
}