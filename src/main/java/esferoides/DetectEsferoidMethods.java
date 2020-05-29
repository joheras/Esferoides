package esferoides;

import java.io.IOException;
import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.Roi;
import ij.measure.Calibration;
import ij.plugin.ImageCalculator;
import ij.plugin.frame.RoiManager;
import ij.process.ImageStatistics;
import loci.formats.FormatException;
import loci.plugins.BF;
import loci.plugins.in.ImporterOptions;

public class DetectEsferoidMethods {

	// Method to detect esferoides.
	public static void detectEsferoideFluoColageno(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {

		ImagePlus impFluo = IJ.openImage(name);

		name = name.replace("fluo", "");
		ImagePlus impNoFluo = IJ.openImage(name);

		String title = impNoFluo.getTitle();

		ImagePlus imp = impNoFluo.duplicate();
		imp.setTitle(title);

		DetectEsferoidImageMethods.processEsferoidFluo(impFluo, true);
		DetectEsferoidImageMethods.processEsferoidNoFluo(impNoFluo);
		ImageCalculator ic = new ImageCalculator();
		ImagePlus imp3 = ic.run("And create", impFluo, impNoFluo);
		IJ.run(imp3, "Fill Holes", "");
		RoiManager rm = AnalyseParticleMethods.analyzeParticlesFluo(imp3);

		imp3.close();
		impFluo.close();
		impNoFluo.close();
		try {
			Utils.showResultsAndSave(dir, name, imp, rm, goodRows);
			imp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Method to detect esferoides.
	public static void detectEsferoideFluoSuspension(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {

		ImagePlus impFluo = IJ.openImage(name);

		name = name.replace("fluo", "");
		ImagePlus impNoFluo = IJ.openImage(name);

		DetectEsferoidImageMethods.processEsferoidFluo(impFluo, false);
		RoiManager rm = AnalyseParticleMethods.analyzeParticlesFluo(impFluo);

		impFluo.close();

		try {
			Utils.showResultsAndSave(dir, name, impNoFluo, rm, goodRows);
			impNoFluo.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Method to detect esferoides.
	public static void detectEsferoideHectorv2(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {
		ImagePlus impb = IJ.openImage(name);
		String title = impb.getTitle();

		ImagePlus imp = impb.duplicate();
		imp.setTitle(title);
		IJ.run(imp, "8-bit", "");
		ImagePlus imp2 = imp.duplicate();
		imp2.setTitle(title);
		RoiManager rm = null;
//
		DetectEsferoidImageMethods.processEsferoidUsingThreshold(imp2, true);
		rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);
		if (rm == null || rm.getRoisAsArray().length == 0) {
			DetectEsferoidImageMethods.processEsferoidUsingThreshold(imp2, false);
			rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);
		}

		try {
			Utils.showResultsAndSave(dir, name, imp, rm, goodRows);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imp.close();

	}

	// Method to detect esferoides.
	public static void detectEsferoideHectorv1(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {
		ImagePlus impb = IJ.openImage(name);
		String title = impb.getTitle();

		ImagePlus imp = impb.duplicate();
		imp.setTitle(title);
		IJ.run(imp, "8-bit", "");
		ImagePlus imp2 = imp.duplicate();
		imp2.setTitle(title);
		RoiManager rm = null;
		//
		DetectEsferoidImageMethods.processEsferoidUsingThreshold(imp2, true);
		rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);
		if (rm == null || rm.getRoisAsArray().length == 0) {
			DetectEsferoidImageMethods.processEsferoidUsingThreshold(imp2, false);
			rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);
		}

		if (rm == null || rm.getRoisAsArray().length == 0) {
			double v = 1.75;

			while ((rm == null || rm.getRoisAsArray().length == 0) && v >= 1.0) {
				imp2 = imp.duplicate();
				DetectEsferoidImageMethods.processEsferoidesGeneralCaseHector(imp2, 3, v);
				rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);
				v = v - 0.25;
			}
		}

		if (rm == null || rm.getRoisAsArray().length == 0) {
			double v = 1.75;
			while ((rm == null || rm.getRoisAsArray().length == 0) && v >= 1.0) {
				imp2 = imp.duplicate();
				DetectEsferoidImageMethods.processEsferoidesGeneralCaseHector(imp2, 5, v);
				rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);
				v = v - 0.25;
			}
		}

		if (rm == null || rm.getRoisAsArray().length == 0) {
			double v = 1.75;
			while ((rm == null || rm.getRoisAsArray().length == 0) && v >= 1.0) {
				imp2 = imp.duplicate();
				DetectEsferoidImageMethods.processEsferoidesGeneralCaseHector(imp2, 7, v);
				rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);
				v = v - 0.25;
			}
		}

		if (rm == null || rm.getRoisAsArray().length == 0) {
			imp2 = imp.duplicate();
			DetectEsferoidImageMethods.processEsferoidUsingThreshold(imp2, false);
			rm = AnalyseParticleMethods.analyzeParticlesHector(imp2);

		}

		try {
			Utils.showResultsAndSave(dir, name, imp, rm, goodRows);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imp.close();

	}

	// Method to detect esferoides.
	public static void detectEsferoideTeodora(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {
		options.setId(name);

		ImagePlus[] imps;
		try {
			imps = BF.openImagePlus(options);

			ImagePlus imp = imps[0];
			ImagePlus imp2 = imp.duplicate();

			RoiManager rm;

			DetectEsferoidImageMethods.processEsferoidEdges(imp2, 0);
			rm = AnalyseParticleMethods.analyseParticlesTeodora(imp2, false, true);
			int iters = 1;
			while ((rm == null || rm.getRoisAsArray().length == 0) && iters < 7) {
				DetectEsferoidImageMethods.processEsferoidEdges(imp2, iters);
				rm = AnalyseParticleMethods.analyseParticlesTeodora(imp2, false, true);
				iters++;
			}

			Utils.showResultsAndSave(dir, name, imp, rm, goodRows);
			imp.close();
		} catch (FormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Method to detect esferoides.
	public static void detectEsferoideTeodoraBig(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {
		options.setId(name);

		ImagePlus[] imps;
		try {
			imps = BF.openImagePlus(options);

			ImagePlus imp = imps[0];
			ImagePlus imp2 = imp.duplicate();

			RoiManager rm;

			int count = Utils.countBelowThreshold(imp2, 1100);
			boolean realBlackHole1 = Utils.countBetweenThresholdOver(imp2, 1100, 2000, 1500);
			boolean realBlackHole2 = Utils.countBelowThreshold(imp2, 3000) < 200000;

			if (count > 100 && realBlackHole2 && realBlackHole1) {

				if (count > 10000) {
					DetectEsferoidImageMethods.processBlackHoles(imp2, false);
				} else {
					DetectEsferoidImageMethods.processBlackHoles(imp2, true);
				}
				rm = AnalyseParticleMethods.analyseParticlesTeodora(imp2, true, true);
			} else {
				DetectEsferoidImageMethods.processEsferoidBig(imp2);
				rm = AnalyseParticleMethods.analyseParticlesTeodora(imp2, false, false);
			}

			imp2 = imp.duplicate();
			int iters = 0;
			while ((rm == null || rm.getRoisAsArray().length == 0) && iters < 7) {
				DetectEsferoidImageMethods.processEsferoidEdges(imp2, iters);
				rm = AnalyseParticleMethods.analyseParticlesTeodora(imp2, false, false);
				iters++;
			}

			Utils.showResultsAndSave(dir, name, imp, rm, goodRows);
			imp.close();
		} catch (FormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Method to detect esferoides.
	public static void detectEsferoideFluoStack(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {
		options.setId(name);

		ImagePlus[] imps;
		try {
			imps = BF.openImagePlus(options);

			ImagePlus imp = imps[0];

			ImageStack stack = imp.getStack();

			Calibration cal = imp.getCalibration();
			ImagePlus impFluo = new ImagePlus(stack.getSliceLabel(2), stack.getProcessor(2));
			impFluo.setCalibration(cal);

			ImagePlus impNoFluo = new ImagePlus(stack.getSliceLabel(1), stack.getProcessor(1));
			impFluo.setCalibration(cal);

			imp = impNoFluo.duplicate();

			ImagePlus impFluoD = impFluo.duplicate();
			DetectEsferoidImageMethods.processEsferoidFluo(impFluoD, true);
			RoiManager rm = AnalyseParticleMethods.analyzeParticlesHector(impFluoD);
			Utils.keepBiggestROI(rm);
			Roi r = rm.getRoi(0);
			ImageStatistics stats = r.getStatistics();
			impFluoD.close();

			if (stats.area > 10000) {
				rm.runCommand("Select All");
				rm.runCommand("Delete");

				ImagePlus impNoFluoD = impNoFluo.duplicate();
				DetectEsferoidImageMethods.processEsferoidNoFluoThreshold(impNoFluoD);
				rm = AnalyseParticleMethods.analyzeParticlesHector(impNoFluoD);
				Utils.keepBiggestROI(rm);

				double round = 0;

				if (rm.getRoisAsArray().length > 0) {
					r = rm.getRoi(0);
					stats = r.getStatistics();
					impNoFluoD.close();
					round = 4.0 * (stats.area / (Math.PI * stats.major * stats.major));
				}
				if (round < 0.9) {
					rm.runCommand("Select All");
					rm.runCommand("Delete");
					System.out.println("Round less than 0.9");
					impFluoD = impFluo.duplicate();
					DetectEsferoidImageMethods.processEsferoidFluo(impFluoD, true);
					DetectEsferoidImageMethods.processEsferoidNoFluoBis(impNoFluo);
					ImageCalculator ic = new ImageCalculator();
					ImagePlus imp3 = ic.run("And create", impFluoD, impNoFluo);
					IJ.run(imp3, "Fill Holes", "");

					imp3 = ic.run("ADD create", imp3, impFluoD);
					rm = AnalyseParticleMethods.analyzeParticlesFluo(imp3);
					imp3.close();
					impFluoD.close();
					impNoFluo.close();

				}

			}

			Utils.showResultsAndSave(dir, name, imp, rm, goodRows);
			imp.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Method to detect esferoides.
	public static void detectEsferoideTeniposide(ImporterOptions options, String dir, String name,
			ArrayList<Integer> goodRows) {

		ImagePlus imp = IJ.openImage(name);

		ImagePlus impD = imp.duplicate();

		DetectEsferoidImageMethods.processEsferoidEdgesThreshold(impD, 22, 255);
		RoiManager rm = AnalyseParticleMethods.analyzeParticlesFluo(impD);
		
		if (rm.getRoisAsArray().length > 0) {
			Roi r = rm.getRoi(0);
			ImageStatistics stats = r.getStatistics();
			double solidity = (stats.area / Utils.getArea(r.getConvexHull()));
			if(solidity<0.8) {
				impD = imp.duplicate();
				DetectEsferoidImageMethods.processEsferoidEdgesThresholdDilateErode(impD, 22, 255);
				rm = AnalyseParticleMethods.analyzeParticlesFluo(impD);
			}
		}

		imp.close();

		try {
			Utils.showResultsAndSave(dir, name, imp, rm, goodRows);
			imp.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
