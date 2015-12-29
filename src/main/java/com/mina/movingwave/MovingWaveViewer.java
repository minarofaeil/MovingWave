package com.mina.movingwave;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author Mina
 */
public class MovingWaveViewer {
	public static void main(String[] args) {
		JFrame testFrame = new JFrame();
		
		final WavePanel wavePanel = new WavePanel();
		new Thread() {
			@Override
			@SuppressWarnings("SleepWhileInLoop")
			public void run() {
				while (true) {
					try {
						wavePanel.setPhase(wavePanel.getPhase() + wavePanel.getPhaseShift());
						wavePanel.repaint();
						Thread.sleep(60);
					} catch (InterruptedException ex) {
						Logger.getLogger(MovingWaveViewer.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}.start();
		
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setSize(800, 600);
		testFrame.setLocationRelativeTo(null);
		testFrame.setContentPane(wavePanel);
		
		testFrame.setVisible(true);
	}
}
