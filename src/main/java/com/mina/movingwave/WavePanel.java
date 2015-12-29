package com.mina.movingwave;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class WavePanel extends JPanel {
	private static final int DISTANCE = 4;
	private static final int RADIUS = 30;
	private static final int DOT_RADIUS = 4;
	
	private double phase = 0;
	private double phaseShift = 30.0 * Math.PI / 180.0;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int width = getWidth();
		int height = getHeight();
		
		for (int x = -RADIUS; x < width; x += RADIUS + DISTANCE) {
			for (int y = -RADIUS; y < height; y += RADIUS + DISTANCE) {
				g.setColor(Color.BLACK);
				g.drawOval(x, y, 2 * RADIUS, 2 * RADIUS);
				
				double currentPhase = phase + phaseShift * (((x + RADIUS) / (RADIUS + DISTANCE)) + ((y + RADIUS) / (RADIUS + DISTANCE)));
				g.setColor(Color.RED);
				g.fillOval((int) (x + RADIUS + RADIUS * Math.cos(currentPhase) - DOT_RADIUS), (int) (y + RADIUS + RADIUS * Math.sin(currentPhase) - DOT_RADIUS), 2 * DOT_RADIUS, 2 * DOT_RADIUS);
			}
		}
	}

	public double getPhase() {
		return phase;
	}

	public void setPhase(double phase) {
		this.phase = phase;
	}

	public double getPhaseShift() {
		return phaseShift;
	}

	public void setPhaseShift(double phaseShift) {
		this.phaseShift = phaseShift;
	}
}
