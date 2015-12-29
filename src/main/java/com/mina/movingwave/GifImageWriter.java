package com.mina.movingwave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;

/**
 *
 * @author Mina
 */
public class GifImageWriter {
	public static void main(String[] args) throws IOException {
		WavePanel wavePanel = new WavePanel();
		wavePanel.setSize(800, 600);

		Iterator<ImageWriter> imageWriterIterator = ImageIO.getImageWritersByFormatName("gif");
		if (imageWriterIterator.hasNext()) {
			ImageWriter gifWriter = imageWriterIterator.next();
			try (FileOutputStream fileOutputStream = new FileOutputStream("wave.gif");
				ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(fileOutputStream)) {
				gifWriter.setOutput(imageOutputStream);
				gifWriter.prepareWriteSequence(null);

				for (double i = 0; i < 2 * Math.PI; i += wavePanel.getPhaseShift()) {
					wavePanel.setPhase(i);

					BufferedImage bufferedImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
					Graphics graphics = bufferedImage.createGraphics();
					wavePanel.paint(graphics);

					ImageWriteParam imageWriteParam = gifWriter.getDefaultWriteParam();
					IIOMetadata metadata = gifWriter.getDefaultImageMetadata(new ImageTypeSpecifier(bufferedImage), imageWriteParam);

					// Make it loop
					if (i == 0) {
						String nativeMetadataFormatName = metadata.getNativeMetadataFormatName();
						Node root = metadata.getAsTree(nativeMetadataFormatName);
						Node child = root.getFirstChild();
						while (child != null) {
							if ("GraphicControlExtension".equals(child.getNodeName())) {
								break;
							}
							child = child.getNextSibling();
						}

						IIOMetadataNode applicationExtensionsNode = new IIOMetadataNode("ApplicationExtensions");
						IIOMetadataNode applicationExtensionNode = new IIOMetadataNode("ApplicationExtension");
						applicationExtensionNode.setAttribute("applicationID", "NETSCAPE");
						applicationExtensionNode.setAttribute("authenticationCode", "2.0");
						byte[] bytes = new byte[]{0x1, 0, 0};
						applicationExtensionNode.setUserObject(bytes);
						applicationExtensionsNode.appendChild(applicationExtensionNode);
						root.appendChild(applicationExtensionsNode);
						
						metadata.setFromTree(nativeMetadataFormatName, root);
					}


					IIOImage frame = new IIOImage(bufferedImage, null, metadata);
					gifWriter.writeToSequence(frame, imageWriteParam);
				}

				gifWriter.endWriteSequence();
			}
		} else {
			System.err.println("Can\'t find gif writer!");
		}
	}
}
