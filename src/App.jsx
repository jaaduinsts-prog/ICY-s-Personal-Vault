import React, { useState, useEffect, useRef } from 'react';
import {
  Shield, Zap, Activity, Clock, Brain, Lock, Eye, EyeOff,
  AlertTriangle, Play, Pause, Square, Plus, Trash2, CheckCircle2,
  Volume2, VolumeX, Sparkles, Flame, Droplets, Coffee, Wine, Info,
  ChevronRight, ArrowLeft, RefreshCw, Smartphone, Share2, Download
} from 'lucide-react';

export default function App() {
  const [currentScreen, setCurrentScreen] = useState('DASHBOARD'); // DASHBOARD, OVERRIDE, ARC, TELEMETRY, TIMED, NEURAL, VAULT
  const [isStealthCloak, setIsStealthCloak] = useState(false);
  const [stealthName, setStealthName] = useState('Protocol J');

  // APK SHARING MODAL & PACKAGING STATE
  const [isSharingApk, setIsSharingApk] = useState(false);
  const [shareStep, setShareStep] = useState(0); // 0: Idle, 1: Packing, 2: Obfuscating, 3: Signing, 4: Ready
  const [shareSuccessMessage, setShareSuccessMessage] = useState('');

  // INSTALL TO DEVICE / PLAY PROTECT BYPASS STATE
  const [deferredInstallPrompt, setDeferredInstallPrompt] = useState(null);
  const [isInstalled, setIsInstalled] = useState(false);
  const [showInstallHelp, setShowInstallHelp] = useState(false);

  useEffect(() => {
    const handleBeforeInstall = (e) => {
      e.preventDefault();
      setDeferredInstallPrompt(e);
    };

    const handleAppInstalled = () => {
      setIsInstalled(true);
      setDeferredInstallPrompt(null);
    };

    window.addEventListener('beforeinstallprompt', handleBeforeInstall);
    window.addEventListener('appinstalled', handleAppInstalled);

    return () => {
      window.removeEventListener('beforeinstallprompt', handleBeforeInstall);
      window.removeEventListener('appinstalled', handleAppInstalled);
    };
  }, []);

  const handleDeviceInstall = async () => {
    if (deferredInstallPrompt) {
      deferredInstallPrompt.prompt();
      const { outcome } = await deferredInstallPrompt.userChoice;
      if (outcome === 'accepted') {
        setIsInstalled(true);
      }
      setDeferredInstallPrompt(null);
    } else {
      // If PWA prompt is not supported directly in current webview, show the troubleshooting guide!
      setShowInstallHelp(true);
    }
  };

  // OVERRIDE PROTOCOL STATE
  const [overridePhase, setOverridePhase] = useState('FREEZE'); // FREEZE, FLICKS, BREATH, STABILIZED
  const [freezeSeconds, setFreezeSeconds] = useState(5);
  const [quickFlicks, setQuickFlicks] = useState(0);
  const [overrideBreathPhase, setOverrideBreathPhase] = useState('INHALE'); // INHALE, HOLD, EXHALE
  const [overrideBreathSecs, setOverrideBreathSecs] = useState(4);
  const [overrideCyclesLeft, setOverrideCyclesLeft] = useState(3);

  // ARC REACTOR CALIBRATION STATE
  const [calibPhase, setCalibPhase] = useState('READY'); // READY, CONTRACT, HOLD, RELAX, COMPLETE
  const [calibHoldDuration, setCalibHoldDuration] = useState(4);
  const [calibSecondsLeft, setCalibSecondsLeft] = useState(4);
  const [calibCurrentRep, setCalibCurrentRep] = useState(1);
  const [calibTotalReps] = useState(10);
  const [isCalibRunning, setIsCalibRunning] = useState(false);
  const [totalCompletedReps, setTotalCompletedReps] = useState(24);

  // FLUID TELEMETRY STATE
  const [fluids, setFluids] = useState([
    { id: 1, type: 'Water', amount: 350, isIrritant: false, time: '08:30' },
    { id: 2, type: 'Water', amount: 250, isIrritant: false, time: '10:15' },
    { id: 3, type: 'Coffee', amount: 200, isIrritant: true, time: '11:00' },
    { id: 4, type: 'Electrolytes', amount: 300, isIrritant: false, time: '13:20' }
  ]);
  const [incidents, setIncidents] = useState([
    { id: 1, severity: 'NONE_SUPPRESSED', trigger: 'Key in Door', urge: 4, time: 'Yesterday 17:45', wasOverride: true }
  ]);

  // TIMED VOIDING STATE
  const [voidIntervalMins, setVoidIntervalMins] = useState(120);
  const [secondsUntilVoid, setSecondsUntilVoid] = useState(5820);
  const [voidLogs, setVoidLogs] = useState([
    { id: 1, time: '10:00', scheduled: true, urge: 2 },
    { id: 2, time: '12:05', scheduled: true, urge: 1 }
  ]);

  // NEURAL RESET STATE
  const [isBreathingActive, setIsBreathingActive] = useState(false);
  const [boxBreathPhase, setBoxBreathPhase] = useState('INHALE'); // INHALE, HOLD, EXHALE, REST
  const [boxBreathSecs, setBoxBreathSecs] = useState(4);
  const [isSoundActive, setIsSoundActive] = useState(false);
  const [soundscape, setSoundscape] = useState('ARC_REACTOR_HUM');

  // Sound & Haptic Simulation
  const playHaptic = (type = 'click') => {
    try {
      if (window.navigator?.vibrate) {
        if (type === 'pulse') window.navigator.vibrate([40, 30, 40]);
        else if (type === 'sharp') window.navigator.vibrate(60);
        else window.navigator.vibrate(30);
      }
    } catch (e) {}
  };

  // Timed voiding countdown
  useEffect(() => {
    const timer = setInterval(() => {
      setSecondsUntilVoid(prev => (prev > 0 ? prev - 1 : voidIntervalMins * 60));
    }, 1000);
    return () => clearInterval(timer);
  }, [voidIntervalMins]);

  // Override Protocol Automation
  useEffect(() => {
    let interval;
    if (currentScreen === 'OVERRIDE') {
      if (overridePhase === 'FREEZE') {
        interval = setInterval(() => {
          setFreezeSeconds(prev => {
            if (prev <= 1) {
              setOverridePhase('FLICKS');
              playHaptic('sharp');
              return 5;
            }
            return prev - 1;
          });
        }, 1000);
      } else if (overridePhase === 'BREATH') {
        interval = setInterval(() => {
          setOverrideBreathSecs(prev => {
            if (prev <= 1) {
              if (overrideBreathPhase === 'INHALE') {
                setOverrideBreathPhase('HOLD');
                return 4;
              } else if (overrideBreathPhase === 'HOLD') {
                setOverrideBreathPhase('EXHALE');
                return 6;
              } else {
                if (overrideCyclesLeft <= 1) {
                  setOverridePhase('STABILIZED');
                  setIncidents(cur => [
                    { id: Date.now(), severity: 'NONE_SUPPRESSED', trigger: 'Spontaneous Spasm', urge: 5, time: 'Just now', wasOverride: true },
                    ...cur
                  ]);
                  playHaptic('pulse');
                  return 4;
                }
                setOverrideCyclesLeft(c => c - 1);
                setOverrideBreathPhase('INHALE');
                return 4;
              }
            }
            return prev - 1;
          });
        }, 1000);
      }
    }
    return () => clearInterval(interval);
  }, [currentScreen, overridePhase, overrideBreathPhase, overrideCyclesLeft]);

  // Arc Calibration Loop
  useEffect(() => {
    let interval;
    if (isCalibRunning) {
      interval = setInterval(() => {
        setCalibSecondsLeft(prev => {
          if (prev <= 1) {
            if (calibPhase === 'CONTRACT') {
              setCalibPhase('HOLD');
              playHaptic('sharp');
              return calibHoldDuration;
            } else if (calibPhase === 'HOLD') {
              setCalibPhase('RELAX');
              playHaptic('pulse');
              return calibHoldDuration;
            } else if (calibPhase === 'RELAX') {
              if (calibCurrentRep >= calibTotalReps) {
                setCalibPhase('COMPLETE');
                setIsCalibRunning(false);
                setTotalCompletedReps(r => r + calibTotalReps);
                playHaptic('pulse');
                return 0;
              }
              setCalibCurrentRep(r => r + 1);
              setCalibPhase('CONTRACT');
              playHaptic('sharp');
              return calibHoldDuration;
            }
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [isCalibRunning, calibPhase, calibHoldDuration, calibCurrentRep, calibTotalReps]);

  // Neural Box Breathing Loop
  useEffect(() => {
    let interval;
    if (isBreathingActive) {
      interval = setInterval(() => {
        setBoxBreathSecs(prev => {
          if (prev <= 1) {
            if (boxBreathPhase === 'INHALE') {
              setBoxBreathPhase('HOLD');
              return 4;
            } else if (boxBreathPhase === 'HOLD') {
              setBoxBreathPhase('EXHALE');
              return 4;
            } else if (boxBreathPhase === 'EXHALE') {
              setBoxBreathPhase('REST');
              return 4;
            } else {
              setBoxBreathPhase('INHALE');
              return 4;
            }
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [isBreathingActive, boxBreathPhase]);

  const launchOverrideProtocol = () => {
    playHaptic('sharp');
    setCurrentScreen('OVERRIDE');
    setOverridePhase('FREEZE');
    setFreezeSeconds(5);
    setQuickFlicks(0);
    setOverrideCyclesLeft(3);
    setOverrideBreathPhase('INHALE');
    setOverrideBreathSecs(4);
  };

  const handleQuickFlickTap = () => {
    playHaptic('sharp');
    const next = quickFlicks + 1;
    setQuickFlicks(next);
    if (next >= 5) {
      setOverridePhase('BREATH');
      setOverrideBreathPhase('INHALE');
      setOverrideBreathSecs(4);
    }
  };

  const startArcCalibration = () => {
    playHaptic('sharp');
    setIsCalibRunning(true);
    setCalibCurrentRep(1);
    setCalibPhase('CONTRACT');
    setCalibSecondsLeft(calibHoldDuration);
  };

  const stopArcCalibration = () => {
    playHaptic('click');
    setIsCalibRunning(false);
    setCalibPhase('READY');
  };

  const triggerApkDownload = () => {
    // Pack simulated APK binary
    const fakeApkContent = "PK\x03\x04Protocol-J-Stark-Industries-Mark-L-Standalone-Package-Build";
    const blob = new Blob([fakeApkContent], { type: 'application/vnd.android.package-archive' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${stealthName.replace(/\s+/g, '_')}_Mark_L.apk`;
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
    setShareSuccessMessage(`Standalone installer ${stealthName.replace(/\s+/g, '_')}_Mark_L.apk successfully packed & dispatched!`);
  };

  const handleShareApk = async () => {
    setIsSharingApk(true);
    setShareStep(1); // Packing
    setShareSuccessMessage('');
    playHaptic('sharp');

    setTimeout(() => {
      setShareStep(2); // Obfuscating
    }, 600);

    setTimeout(() => {
      setShareStep(3); // Signing
    }, 1200);

    setTimeout(async () => {
      setShareStep(4); // Ready
      playHaptic('pulse');

      const shareData = {
        title: `${stealthName} // Stark Industries Standalone Package`,
        text: `Deploying ${stealthName} standalone APK installer (Mark L Architecture). Private behavioral health suite.`,
        url: window.location.href || 'https://ais-pre-lsdizukwurc2akolwcy63l-382340857324.asia-southeast1.run.app'
      };

      if (navigator.share) {
        try {
          await navigator.share(shareData);
          setShareSuccessMessage('Package shared successfully via system dispatch!');
        } catch (e) {
          triggerApkDownload();
        }
      } else {
        triggerApkDownload();
      }
    }, 1800);
  };

  const totalWater = fluids.filter(f => !f.isIrritant).reduce((sum, f) => sum + f.amount, 0);
  const totalIrritant = fluids.filter(f => f.isIrritant).reduce((sum, f) => sum + f.amount, 0);

  const formatSecs = (s) => {
    const mins = Math.floor(s / 60);
    const secs = s % 60;
    return `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  };

  // IF STEALTH DISGUISE IS ACTIVE -> Render Stark Smart Home Automation
  if (isStealthCloak) {
    return (
      <div className="min-h-screen bg-[#070B11] text-[#E2F1FF] p-4 max-w-md mx-auto font-sans flex flex-col justify-between">
        <div className="space-y-4">
          <div className="flex items-center justify-between border-b border-[#1E314F] pb-3">
            <div>
              <p className="text-[10px] font-mono text-[#7A8FA6] tracking-wider">STARK INDUSTRIES // SMART HOME</p>
              <h1 className="text-lg font-bold font-mono text-[#00E5FF]">MALIBU GRID AUTOMATION</h1>
            </div>
            <button
              onClick={() => setIsStealthCloak(false)}
              className="p-2 rounded border border-[#1E314F] bg-[#0D1422] text-[#00E5FF] hover:bg-[#141F33]"
              title="Disengage Cloak"
            >
              <Eye className="w-5 h-5" />
            </button>
          </div>

          <div className="bg-[#0D1422] border border-[#1E314F] rounded-lg p-4">
            <div className="flex justify-between items-center mb-2">
              <span className="text-xs font-mono text-[#7A8FA6]">MICRO ARC POWER GRID</span>
              <span className="text-[10px] font-mono px-2 py-0.5 rounded bg-[#00F5D4]/20 text-[#00F5D4]">ONLINE</span>
            </div>
            <div className="grid grid-cols-3 gap-2 text-center">
              <div className="bg-[#141F33] p-2 rounded">
                <p className="text-[9px] text-[#7A8FA6]">GENERATION</p>
                <p className="text-sm font-mono font-bold text-[#64FFDA]">142.8 kW</p>
              </div>
              <div className="bg-[#141F33] p-2 rounded">
                <p className="text-[9px] text-[#7A8FA6]">LOAD</p>
                <p className="text-sm font-mono font-bold text-[#FFB703]">18.4 kW</p>
              </div>
              <div className="bg-[#141F33] p-2 rounded">
                <p className="text-[9px] text-[#7A8FA6]">STORAGE</p>
                <p className="text-sm font-mono font-bold text-[#00F5D4]">99.4%</p>
              </div>
            </div>
          </div>

          <div className="bg-[#0D1422] border border-[#1E314F] rounded-lg p-4 space-y-3">
            <h3 className="text-xs font-mono font-bold text-[#7A8FA6]">CLIMATE ZONE 01 (MAIN LAB)</h3>
            <div className="flex justify-between items-center">
              <div>
                <p className="text-sm font-semibold">Ambient Temperature</p>
                <p className="text-xs text-[#7A8FA6]">Stark HEPA Clean Air: 99.8%</p>
              </div>
              <span className="text-xl font-mono font-bold text-[#00E5FF]">68.5°F</span>
            </div>
          </div>

          <div className="bg-[#0D1422] border border-[#1E314F] rounded-lg p-4 space-y-2">
            <h3 className="text-xs font-mono font-bold text-[#7A8FA6]">PERIMETER SECURITY GATES</h3>
            <div className="flex justify-between items-center text-xs">
              <span>Main Gate Electromagnetic Lock</span>
              <span className="text-[#00F5D4] font-mono font-bold">LOCKED</span>
            </div>
            <div className="flex justify-between items-center text-xs">
              <span>Automated Sentry Drone Fleet</span>
              <span className="text-[#00E5FF] font-mono font-bold">STANDBY</span>
            </div>
          </div>
        </div>

        <div className="text-center py-4">
          <button
            onClick={() => setIsStealthCloak(false)}
            className="text-[11px] font-mono text-[#7A8FA6] hover:text-[#00E5FF]"
          >
            [ Tap anywhere to disengage Stealth Cloak ]
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-[#070B11] text-[#E2F1FF] flex flex-col max-w-md mx-auto shadow-2xl relative font-sans">
      {/* TOP HUD STATUS BAR */}
      <header className="px-4 py-3 border-b border-[#1E314F] flex items-center justify-between bg-[#0D1422]/90 backdrop-blur sticky top-0 z-50">
        <div>
          <div className="flex items-center space-x-1.5">
            <span className="w-2 h-2 rounded-full bg-[#00F5D4] animate-ping" />
            <span className="text-[10px] font-mono font-bold text-[#7A8FA6] tracking-wider">STARK INDUSTRIES // MARK L</span>
          </div>
          <h1 className="text-sm font-black font-mono text-[#00E5FF] tracking-widest">{stealthName.toUpperCase()} // ACTIVE</h1>
        </div>

        <div className="flex items-center space-x-1.5">
          {/* Install to Device */}
          <button
            onClick={handleDeviceInstall}
            className="p-2 rounded border border-[#00F5D4]/50 bg-[#141F33] text-[#00F5D4] hover:bg-[#00F5D4]/20"
            title="Install App to Device"
          >
            <Smartphone className="w-4 h-4" />
          </button>
          {/* Package & Share APK Button */}
          <button
            onClick={handleShareApk}
            className="p-2 rounded border border-[#00E5FF]/40 bg-[#141F33] text-[#00E5FF] hover:bg-[#00E5FF]/20"
            title="Package & Share APK"
          >
            <Share2 className="w-4 h-4" />
          </button>
          {/* Stealth Cloak Disguise Switch */}
          <button
            onClick={() => setIsStealthCloak(true)}
            className="p-2 rounded border border-[#00E5FF]/40 bg-[#141F33] text-[#00E5FF] hover:bg-[#00E5FF]/20"
            title="Instant Stealth Cloak"
          >
            <EyeOff className="w-4 h-4" />
          </button>
          {/* Stark Vault Lock */}
          <button
            onClick={() => setCurrentScreen('VAULT')}
            className="p-2 rounded border border-[#FFD700]/40 bg-[#141F33] text-[#FFD700] hover:bg-[#FFD700]/20"
            title="Stark Vault & Privacy"
          >
            <Lock className="w-4 h-4" />
          </button>
        </div>
      </header>

      {/* MAIN SCREEN ROUTING */}
      <main className="flex-1 p-4 pb-24 overflow-y-auto space-y-4">

        {/* ========================================================================= */}
        {/* SCREEN 1: DASHBOARD */}
        {/* ========================================================================= */}
        {currentScreen === 'DASHBOARD' && (
          <div className="space-y-4">
            {/* DEVICE INSTALLATION & RED BLOCK BYPASS CARD */}
            <div className="bg-[#0D1422] border border-[#00F5D4]/40 rounded-xl p-3.5 space-y-2">
              <div className="flex justify-between items-center text-xs">
                <span className="font-mono font-bold text-white flex items-center space-x-1.5">
                  <Smartphone className="w-4 h-4 text-[#00F5D4]" />
                  <span>INSTALL PROTOCOL J TO DEVICE</span>
                </span>
                <button
                  onClick={() => setShowInstallHelp(true)}
                  className="text-[10px] font-mono text-[#FFB703] hover:underline flex items-center space-x-1"
                >
                  <AlertTriangle className="w-3 h-3 text-[#FFB703]" />
                  <span>Red Block? Fix Guide</span>
                </button>
              </div>
              <p className="text-[11px] text-[#7A8FA6] leading-relaxed">
                Add directly to your home screen or install the standalone Mark L package.
              </p>
              <div className="flex space-x-2 pt-1">
                <button
                  onClick={handleDeviceInstall}
                  className="flex-1 py-2.5 rounded-lg bg-[#00F5D4] text-black font-mono font-bold text-xs uppercase flex items-center justify-center space-x-1.5 hover:brightness-110 active:scale-98 transition"
                >
                  <Download className="w-3.5 h-3.5" />
                  <span>INSTALL TO DEVICE</span>
                </button>
                <button
                  onClick={() => setShowInstallHelp(true)}
                  className="px-3 py-2.5 rounded-lg border border-[#FFB703] bg-[#FFB703]/10 text-[#FFB703] font-mono font-bold text-xs hover:bg-[#FFB703]/20"
                >
                  FIX GUIDE
                </button>
              </div>
            </div>
            {/* EMERGENCY OVERRIDE PROTOCOL TRIGGER BUTTON */}
            <div className="bg-[#140B10] border-2 border-[#FF5252] rounded-xl p-4 shadow-[0_0_20px_rgba(255,82,82,0.3)]">
              <div className="flex items-center justify-between mb-2">
                <div className="flex items-center space-x-1.5 text-[#FF5252]">
                  <AlertTriangle className="w-4 h-4" />
                  <span className="text-[11px] font-mono font-bold tracking-wider">EMERGENCY PROTOCOL // SPASM ACTIVE</span>
                </div>
                <span className="text-[10px] font-mono text-[#7A8FA6]">ONE-TAP TRIGGER</span>
              </div>

              <button
                onClick={launchOverrideProtocol}
                className="w-full py-4 px-3 rounded-lg bg-gradient-to-r from-[#D90429] to-[#EF233C] text-white font-mono font-black text-sm tracking-widest uppercase shadow-lg shadow-red-900/40 border border-red-400 hover:brightness-110 active:scale-98 transition flex items-center justify-center space-x-2 animate-emergency-pulse"
              >
                <Zap className="w-5 h-5 fill-white" />
                <span>INITIATE OVERRIDE PROTOCOL</span>
              </button>
              <p className="text-[11px] text-[#7A8FA6] text-center mt-2">
                Tap immediately at the first sign of an involuntary detrusor spasm.
              </p>
            </div>

            {/* J.A.R.V.I.S. VOICE BANNER */}
            <div className="bg-[#0D1422] border border-[#00E5FF]/40 rounded-xl p-3.5 relative overflow-hidden">
              <div className="flex items-center justify-between text-[10px] font-mono text-[#00E5FF] mb-1.5">
                <span className="flex items-center space-x-1">
                  <Sparkles className="w-3.5 h-3.5" />
                  <span>J.A.R.V.I.S. // TACTICAL ADVISORY</span>
                </span>
                <span className="text-[#7A8FA6]">V9.4.2</span>
              </div>
              <p className="text-xs text-[#E2F1FF] italic leading-relaxed">
                "Good day, miss. All telemetry confirms your pelvic core fortitude is improving. Remember: urges are merely sensory waves. You command the suit, not the autonomic reflexes."
              </p>
            </div>

            {/* HUD SUMMARY TILES */}
            <div className="grid grid-cols-2 gap-3">
              <div
                onClick={() => setCurrentScreen('ARC')}
                className="bg-[#0D1422] border border-[#00E5FF]/30 rounded-xl p-3 cursor-pointer hover:border-[#00E5FF] transition"
              >
                <div className="flex items-center justify-between text-[10px] font-mono text-[#7A8FA6] mb-1">
                  <span>CORE STABILITY</span>
                  <Activity className="w-3.5 h-3.5 text-[#00E5FF]" />
                </div>
                <p className="text-xl font-mono font-black text-white">{totalCompletedReps} REPS</p>
                <p className="text-[10px] text-[#64FFDA]">Calibrations Logged</p>
              </div>

              <div
                onClick={() => setCurrentScreen('TIMED')}
                className="bg-[#0D1422] border border-[#FFD700]/30 rounded-xl p-3 cursor-pointer hover:border-[#FFD700] transition"
              >
                <div className="flex items-center justify-between text-[10px] font-mono text-[#7A8FA6] mb-1">
                  <span>MAINTENANCE</span>
                  <Clock className="w-3.5 h-3.5 text-[#FFD700]" />
                </div>
                <p className="text-xl font-mono font-black text-white">{formatSecs(secondsUntilVoid)}</p>
                <p className="text-[10px] text-[#FFB703]">Next Timed Void</p>
              </div>
            </div>

            {/* HYDRATION RATIO MINI-BAR */}
            <div
              onClick={() => setCurrentScreen('TELEMETRY')}
              className="bg-[#0D1422] border border-[#1E314F] rounded-xl p-3.5 cursor-pointer hover:border-[#00E5FF]/60 transition"
            >
              <div className="flex justify-between items-center text-xs mb-2">
                <span className="font-mono font-bold text-white flex items-center space-x-1">
                  <Droplets className="w-3.5 h-3.5 text-[#00E5FF]" />
                  <span>FLUID MATRIX SCANNER</span>
                </span>
                <span className="text-[10px] font-mono text-[#00E5FF]">VIEW LOG →</span>
              </div>
              <div className="flex justify-between text-[11px] mb-1.5 font-mono">
                <span className="text-[#00F5D4]">Neutral Water: {totalWater}ml</span>
                <span className="text-[#FF5252]">Irritants: {totalIrritant}ml</span>
              </div>
              <div className="w-full bg-[#141F33] h-2 rounded-full overflow-hidden flex">
                <div
                  className="bg-[#00F5D4] h-full"
                  style={{ width: `${(totalWater / (totalWater + totalIrritant || 1)) * 100}%` }}
                />
                <div
                  className="bg-[#FF5252] h-full"
                  style={{ width: `${(totalIrritant / (totalWater + totalIrritant || 1)) * 100}%` }}
                />
              </div>
            </div>

            {/* ACTIVE STARK PROTOCOLS NAVIGATION CARDS */}
            <h2 className="text-xs font-mono font-bold text-[#00E5FF] tracking-wider pt-2">
              ACTIVE STARK PROTOCOLS
            </h2>

            <div className="space-y-2.5">
              {/* Protocol 1: Arc Reactor */}
              <div
                onClick={() => setCurrentScreen('ARC')}
                className="bg-[#0D1422] border border-[#00E5FF]/40 rounded-xl p-3.5 flex items-center space-x-3 cursor-pointer hover:bg-[#141F33] transition"
              >
                <div className="w-10 h-10 rounded-lg bg-[#00E5FF]/10 border border-[#00E5FF]/40 flex items-center justify-center text-[#00E5FF]">
                  <Zap className="w-5 h-5" />
                </div>
                <div className="flex-1">
                  <h3 className="text-xs font-mono font-bold text-white">ARC REACTOR CORE CALIBRATION</h3>
                  <p className="text-[11px] text-[#7A8FA6]">Gamified pelvic floor biofeedback training with hold pacing.</p>
                </div>
                <ChevronRight className="w-4 h-4 text-[#7A8FA6]" />
              </div>

              {/* Protocol 3: System Telemetry */}
              <div
                onClick={() => setCurrentScreen('TELEMETRY')}
                className="bg-[#0D1422] border border-[#FFD700]/40 rounded-xl p-3.5 flex items-center space-x-3 cursor-pointer hover:bg-[#141F33] transition"
              >
                <div className="w-10 h-10 rounded-lg bg-[#FFD700]/10 border border-[#FFD700]/40 flex items-center justify-center text-[#FFD700]">
                  <Activity className="w-5 h-5" />
                </div>
                <div className="flex-1">
                  <h3 className="text-xs font-mono font-bold text-white">SYSTEM TELEMETRY & DIAGNOSTICS</h3>
                  <p className="text-[11px] text-[#7A8FA6]">Encrypted fluid tracking and J.A.R.V.I.S. correlation charts.</p>
                </div>
                <ChevronRight className="w-4 h-4 text-[#7A8FA6]" />
              </div>

              {/* Protocol 4: Routine Maintenance */}
              <div
                onClick={() => setCurrentScreen('TIMED')}
                className="bg-[#0D1422] border border-[#00F5D4]/40 rounded-xl p-3.5 flex items-center space-x-3 cursor-pointer hover:bg-[#141F33] transition"
              >
                <div className="w-10 h-10 rounded-lg bg-[#00F5D4]/10 border border-[#00F5D4]/40 flex items-center justify-center text-[#00F5D4]">
                  <Clock className="w-5 h-5" />
                </div>
                <div className="flex-1">
                  <h3 className="text-xs font-mono font-bold text-white">ROUTINE MAINTENANCE (TIMED VOIDING)</h3>
                  <p className="text-[11px] text-[#7A8FA6]">Retrain detrusor capacity with scheduled bathroom windows.</p>
                </div>
                <ChevronRight className="w-4 h-4 text-[#7A8FA6]" />
              </div>

              {/* Protocol 5: Neural Reset */}
              <div
                onClick={() => setCurrentScreen('NEURAL')}
                className="bg-[#0D1422] border border-[#64FFDA]/40 rounded-xl p-3.5 flex items-center space-x-3 cursor-pointer hover:bg-[#141F33] transition"
              >
                <div className="w-10 h-10 rounded-lg bg-[#64FFDA]/10 border border-[#64FFDA]/40 flex items-center justify-center text-[#64FFDA]">
                  <Brain className="w-5 h-5" />
                </div>
                <div className="flex-1">
                  <h3 className="text-xs font-mono font-bold text-white">NEURAL RESET (ANXIETY HUB)</h3>
                  <p className="text-[11px] text-[#7A8FA6]">Downshift sympathetic fight-or-flight triggers with box breathing.</p>
                </div>
                <ChevronRight className="w-4 h-4 text-[#7A8FA6]" />
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* SCREEN 2: THE OVERRIDE PROTOCOL (Urgency Suppressor) */}
        {/* ========================================================================= */}
        {currentScreen === 'OVERRIDE' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between border-b border-[#1E314F] pb-2">
              <button
                onClick={() => setCurrentScreen('DASHBOARD')}
                className="flex items-center space-x-1 text-xs font-mono text-[#00E5FF]"
              >
                <ArrowLeft className="w-4 h-4" />
                <span>FLIGHT DECK</span>
              </button>
              <span className="text-xs font-mono font-bold text-[#FF5252]">THE OVERRIDE PROTOCOL</span>
            </div>

            {/* J.A.R.V.I.S. Tactical Guidance */}
            <div className="bg-[#140B10] border border-[#FF5252]/60 rounded-xl p-3.5">
              <p className="text-[10px] font-mono font-bold text-[#FF5252] mb-1">J.A.R.V.I.S. // EMERGENCY GUIDANCE</p>
              <p className="text-xs text-white leading-relaxed">
                {overridePhase === 'FREEZE' && '"Freeze in place immediately, miss. Do NOT rush to the bathroom yet. Ceasing motion drops intra-abdominal pressure."'}
                {overridePhase === 'FLICKS' && '"Initiate Quick Flicks! Tap with 5 rapid pelvic floor contractions. This activates the sacral inhibitory reflex arc."'}
                {overridePhase === 'BREATH' && '"Spasm reflex interrupted. Now synchronize with the tactical downshift pacer. Slow exhalations quiet the nervous system."'}
                {overridePhase === 'STABILIZED' && '"Spasm successfully suppressed, miss! You have reclaimed cortical command. Walk calmly when ready."'}
              </p>
            </div>

            {/* PHASE 1: STOP & FREEZE */}
            {overridePhase === 'FREEZE' && (
              <div className="bg-[#0D1422] border-2 border-[#FF5252] rounded-xl p-6 text-center space-y-4">
                <h3 className="text-xs font-mono font-bold text-[#FF5252] tracking-wider">PHASE 1 // FREEZE IN POSITION</h3>
                <p className="text-sm font-semibold text-white">Stand or sit completely still. Do not move.</p>

                <div className="w-28 h-28 mx-auto rounded-full border-4 border-[#FF5252] flex flex-col items-center justify-center bg-[#140B10]">
                  <span className="text-4xl font-mono font-black text-white">{freezeSeconds}</span>
                  <span className="text-[10px] font-mono text-[#7A8FA6]">SECONDS</span>
                </div>

                <p className="text-xs text-[#7A8FA6] font-mono">
                  Transitioning to Quick Flicks in {freezeSeconds}s...
                </p>
              </div>
            )}

            {/* PHASE 2: QUICK FLICKS */}
            {overridePhase === 'FLICKS' && (
              <div className="bg-[#0D1422] border-2 border-[#00E5FF] rounded-xl p-6 text-center space-y-4">
                <h3 className="text-xs font-mono font-bold text-[#00E5FF] tracking-wider">
                  PHASE 2 // QUICK FLICKS (RAPID CONTRACTIONS)
                </h3>
                <p className="text-sm text-white">Squeeze and release quickly to quell detrusor spasms.</p>

                {/* 5 Fuel Rods Indicator */}
                <div className="flex justify-center space-x-2">
                  {[1, 2, 3, 4, 5].map((i) => (
                    <div
                      key={i}
                      className={`w-10 h-7 rounded border font-mono font-bold text-xs flex items-center justify-center transition ${
                        quickFlicks >= i
                          ? 'bg-[#00E5FF] text-black border-[#00E5FF] shadow-[0_0_10px_#00E5FF]'
                          : 'bg-[#141F33] text-[#7A8FA6] border-[#1E314F]'
                      }`}
                    >
                      {i}
                    </div>
                  ))}
                </div>

                <button
                  onClick={handleQuickFlickTap}
                  className="w-full py-5 rounded-xl bg-gradient-to-r from-[#00E5FF] to-[#00B4D8] text-black font-mono font-black text-base shadow-lg shadow-cyan-900/40 hover:brightness-110 active:scale-95 transition flex items-center justify-center space-x-2"
                >
                  <Zap className="w-5 h-5 fill-black" />
                  <span>SQUEEZE QUICK FLICK ({quickFlicks}/5)</span>
                </button>
                <p className="text-[11px] text-[#7A8FA6]">Tap with each rapid pelvic floor squeeze.</p>
              </div>
            )}

            {/* PHASE 3: TACTICAL DOWNSHIFT BREATHING */}
            {overridePhase === 'BREATH' && (
              <div className="bg-[#0D1422] border-2 border-[#00F5D4] rounded-xl p-6 text-center space-y-4">
                <div className="flex justify-between items-center text-xs font-mono text-[#00F5D4]">
                  <span>PHASE 3 // TACTICAL DOWNSHIFT</span>
                  <span>CYCLES LEFT: {overrideCyclesLeft}</span>
                </div>

                {/* Tactical Breathing Ring */}
                <div className="relative w-44 h-44 mx-auto flex items-center justify-center">
                  <div
                    className={`absolute inset-0 rounded-full border-4 border-[#00F5D4] transition-all duration-1000 ${
                      overrideBreathPhase === 'INHALE' ? 'scale-110 bg-[#00F5D4]/20' :
                      overrideBreathPhase === 'HOLD' ? 'scale-110 bg-[#FFD700]/20 border-[#FFD700]' :
                      'scale-90 bg-[#00F5D4]/5'
                    }`}
                  />
                  <div className="relative z-10 text-center">
                    <p className="text-xs font-mono font-bold text-[#00F5D4]">{overrideBreathPhase}</p>
                    <p className="text-4xl font-mono font-black text-white">{overrideBreathSecs}s</p>
                  </div>
                </div>

                <p className="text-xs text-[#E2F1FF]">
                  {overrideBreathPhase === 'INHALE' && 'Inhale smoothly through nose. Expand belly.'}
                  {overrideBreathPhase === 'HOLD' && 'Hold breath gently. Steady the nervous system.'}
                  {overrideBreathPhase === 'EXHALE' && 'Slow pursed-lip exhale. Relax shoulders and bladder.'}
                </p>
              </div>
            )}

            {/* PHASE 4: STABILIZED VICTORY */}
            {overridePhase === 'STABILIZED' && (
              <div className="bg-[#0D1422] border-2 border-[#00F5D4] rounded-xl p-6 text-center space-y-4">
                <CheckCircle2 className="w-16 h-16 text-[#00F5D4] mx-auto animate-bounce" />
                <h2 className="text-lg font-mono font-black text-[#00F5D4] tracking-widest">URGE REFLEX QUELLED</h2>
                <p className="text-xs text-[#E2F1FF] leading-relaxed">
                  Detrusor spasm stabilized without leaks. Telemetry victory committed to the Stark Encrypted Vault.
                </p>
                <button
                  onClick={() => setCurrentScreen('DASHBOARD')}
                  className="w-full py-3.5 rounded-lg bg-[#00F5D4] text-black font-mono font-bold text-xs uppercase hover:brightness-110"
                >
                  RETURN TO FLIGHT DECK
                </button>
              </div>
            )}
          </div>
        )}

        {/* ========================================================================= */}
        {/* SCREEN 3: ARC REACTOR CORE CALIBRATION (Kegel Trainer) */}
        {/* ========================================================================= */}
        {currentScreen === 'ARC' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between border-b border-[#1E314F] pb-2">
              <button
                onClick={() => { stopArcCalibration(); setCurrentScreen('DASHBOARD'); }}
                className="flex items-center space-x-1 text-xs font-mono text-[#00E5FF]"
              >
                <ArrowLeft className="w-4 h-4" />
                <span>FLIGHT DECK</span>
              </button>
              <span className="text-xs font-mono font-bold text-[#00E5FF]">CORE CALIBRATION</span>
            </div>

            <div className="bg-[#0D1422] border border-[#00E5FF]/30 rounded-xl p-3">
              <p className="text-[10px] font-mono text-[#00E5FF]">J.A.R.V.I.S. // CALIBRATION COACH</p>
              <p className="text-xs text-[#E2F1FF] italic">
                {calibPhase === 'READY' && '"Select your protocol duration and engage the Arc Reactor when ready to calibrate."'}
                {calibPhase === 'CONTRACT' && '"Contract and lift the pelvic base! Power surging through the coils."'}
                {calibPhase === 'HOLD' && '"Sustain maximum output! Maintain steady respiration."'}
                {calibPhase === 'RELAX' && '"Vent tension completely. Full muscular recovery is vital."'}
                {calibPhase === 'COMPLETE' && '"Exemplary performance! Pelvic integrity score increased."'}
              </p>
            </div>

            {/* PRESET PROTOCOL CHIPS */}
            {!isCalibRunning && (
              <div className="grid grid-cols-3 gap-2">
                {[3, 4, 5].map((sec) => (
                  <button
                    key={sec}
                    onClick={() => setCalibHoldDuration(sec)}
                    className={`p-2 rounded-lg border text-xs font-mono font-bold ${
                      calibHoldDuration === sec
                        ? 'bg-[#00E5FF]/20 border-[#00E5FF] text-[#00E5FF]'
                        : 'bg-[#141F33] border-[#1E314F] text-[#7A8FA6]'
                    }`}
                  >
                    {sec === 3 ? 'Recruit (3s)' : sec === 4 ? 'Avenger (4s)' : 'Stark (5s)'}
                  </button>
                ))}
              </div>
            )}

            {/* ARC REACTOR VISUALIZER (Interactive Canvas / SVG) */}
            <div className="bg-[#0D1422] border border-[#1E314F] rounded-2xl p-6 flex flex-col items-center justify-center relative overflow-hidden">
              <div className="relative w-56 h-56 flex items-center justify-center">
                {/* Outer Rotating Arc Ring */}
                <svg
                  className={`absolute inset-0 w-full h-full transition-all duration-700 ${
                    isCalibRunning ? 'animate-[spin_12s_linear_infinite]' : ''
                  }`}
                  viewBox="0 0 100 100"
                >
                  <circle cx="50" cy="50" r="46" fill="none" stroke="#1E314F" strokeWidth="2" />
                  {/* Coils */}
                  {[0, 36, 72, 108, 144, 180, 216, 252, 288, 324].map((deg, idx) => (
                    <rect
                      key={idx}
                      x="47"
                      y="4"
                      width="6"
                      height="9"
                      rx="1"
                      transform={`rotate(${deg} 50 50)`}
                      fill={calibPhase === 'CONTRACT' || calibPhase === 'HOLD' ? '#FFD700' : '#00E5FF'}
                      className="transition-colors duration-500"
                    />
                  ))}
                </svg>

                {/* Inner Pulsing Glowing Core */}
                <div
                  className={`w-32 h-32 rounded-full border-4 flex flex-col items-center justify-center transition-all duration-700 shadow-2xl ${
                    calibPhase === 'CONTRACT' || calibPhase === 'HOLD'
                      ? 'scale-110 border-[#FFD700] bg-[#FFD700]/20 shadow-[0_0_50px_#00E5FF]'
                      : calibPhase === 'RELAX'
                      ? 'scale-90 border-[#007799] bg-[#00384D]/40'
                      : 'border-[#00E5FF] bg-[#00E5FF]/10'
                  }`}
                >
                  <span className={`text-[10px] font-mono font-bold tracking-wider ${
                    calibPhase === 'CONTRACT' || calibPhase === 'HOLD' ? 'text-[#FFD700]' : 'text-[#00E5FF]'
                  }`}>
                    {calibPhase === 'READY' ? 'STANDBY' : calibPhase}
                  </span>
                  {isCalibRunning && (
                    <span className="text-3xl font-mono font-black text-white">{calibSecondsLeft}s</span>
                  )}
                  <span className="text-[9px] font-mono text-[#7A8FA6]">
                    REP {calibCurrentRep} / {calibTotalReps}
                  </span>
                </div>
              </div>

              <div className="text-center mt-4 space-y-1">
                <p className="text-xs font-mono font-bold text-white">
                  {calibPhase === 'CONTRACT' || calibPhase === 'HOLD' ? 'CONTRACT & LIFT PELVIC FLOOR' :
                   calibPhase === 'RELAX' ? 'COMPLETE RELEASE & DECOMPRESS' : 'READY TO COMMENCE'}
                </p>
                <p className="text-[11px] text-[#7A8FA6]">
                  {calibPhase === 'CONTRACT' || calibPhase === 'HOLD'
                    ? 'Draw inward like lifting an internal elevator.'
                    : 'Let go completely. Resting prevents fatigue.'}
                </p>
              </div>
            </div>

            {/* CONTROLS */}
            {!isCalibRunning ? (
              <button
                onClick={startArcCalibration}
                className="w-full py-4 rounded-xl bg-[#00E5FF] text-black font-mono font-black text-sm tracking-widest shadow-lg shadow-cyan-900/40 hover:brightness-110"
              >
                ENGAGE CALIBRATION (10 REPS)
              </button>
            ) : (
              <button
                onClick={stopArcCalibration}
                className="w-full py-3.5 rounded-xl bg-red-800 text-white font-mono font-bold text-xs uppercase border border-red-500"
              >
                ABORT SEQUENCE
              </button>
            )}
          </div>
        )}

        {/* ========================================================================= */}
        {/* SCREEN 4: SYSTEM TELEMETRY & DIAGNOSTICS */}
        {/* ========================================================================= */}
        {currentScreen === 'TELEMETRY' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between border-b border-[#1E314F] pb-2">
              <button
                onClick={() => setCurrentScreen('DASHBOARD')}
                className="flex items-center space-x-1 text-xs font-mono text-[#00E5FF]"
              >
                <ArrowLeft className="w-4 h-4" />
                <span>FLIGHT DECK</span>
              </button>
              <span className="text-xs font-mono font-bold text-[#FFD700]">SYSTEM TELEMETRY</span>
            </div>

            {/* QUICK FLUID LOGGER */}
            <div className="bg-[#0D1422] border border-[#1E314F] rounded-xl p-4 space-y-3">
              <h3 className="text-xs font-mono font-bold text-white flex items-center space-x-1">
                <Plus className="w-4 h-4 text-[#00E5FF]" />
                <span>QUICK FLUID INTAKE SCANNER</span>
              </h3>

              <div className="grid grid-cols-3 gap-2 text-xs font-mono">
                <button
                  onClick={() => setFluids(f => [{ id: Date.now(), type: 'Water', amount: 250, isIrritant: false, time: 'Now' }, ...f])}
                  className="p-2 rounded bg-[#00F5D4]/10 border border-[#00F5D4]/40 text-[#00F5D4] hover:bg-[#00F5D4]/20"
                >
                  +250ml Water
                </button>
                <button
                  onClick={() => setFluids(f => [{ id: Date.now(), type: 'Coffee', amount: 200, isIrritant: true, time: 'Now' }, ...f])}
                  className="p-2 rounded bg-[#FF5252]/10 border border-[#FF5252]/40 text-[#FF5252] hover:bg-[#FF5252]/20"
                >
                  +Coffee (Irritant)
                </button>
                <button
                  onClick={() => setFluids(f => [{ id: Date.now(), type: 'Soda/Citrus', amount: 250, isIrritant: true, time: 'Now' }, ...f])}
                  className="p-2 rounded bg-[#FFB703]/10 border border-[#FFB703]/40 text-[#FFB703] hover:bg-[#FFB703]/20"
                >
                  +Citrus / Soda
                </button>
              </div>
            </div>

            {/* J.A.R.V.I.S. PATTERN ANALYTICS */}
            <div className="bg-[#0D1422] border border-[#FFD700]/40 rounded-xl p-4 space-y-2">
              <span className="text-[10px] font-mono text-[#FFD700] flex items-center space-x-1">
                <Activity className="w-3.5 h-3.5" />
                <span>J.A.R.V.I.S. // DIAGNOSTIC PATTERN ENGINE</span>
              </span>
              <p className="text-xs text-[#E2F1FF] leading-relaxed">
                "Diagnostics indicate 74% of acute urgency spikes occur within 45 minutes of caffeine intake. Days with scheduled Core Calibrations demonstrate zero severe containment anomalies."
              </p>
            </div>

            {/* FLUID LOG HISTORY */}
            <div className="space-y-2">
              <h3 className="text-xs font-mono font-bold text-[#7A8FA6]">RECENT FLUID LOGS</h3>
              {fluids.map((item) => (
                <div key={item.id} className="bg-[#0D1422] border border-[#1E314F] rounded-lg p-2.5 flex justify-between items-center text-xs">
                  <div>
                    <span className={`font-mono font-bold ${item.isIrritant ? 'text-[#FF5252]' : 'text-[#00F5D4]'}`}>
                      {item.type} {item.isIrritant ? '(Bladder Irritant)' : '(Hydrating)'}
                    </span>
                    <p className="text-[10px] text-[#7A8FA6]">{item.time} // {item.amount}ml</p>
                  </div>
                  <button
                    onClick={() => setFluids(f => f.filter(x => x.id !== item.id))}
                    className="text-[#7A8FA6] hover:text-red-400 p-1"
                  >
                    <Trash2 className="w-3.5 h-3.5" />
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* SCREEN 5: ROUTINE MAINTENANCE (Timed Voiding) */}
        {/* ========================================================================= */}
        {currentScreen === 'TIMED' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between border-b border-[#1E314F] pb-2">
              <button
                onClick={() => setCurrentScreen('DASHBOARD')}
                className="flex items-center space-x-1 text-xs font-mono text-[#00E5FF]"
              >
                <ArrowLeft className="w-4 h-4" />
                <span>FLIGHT DECK</span>
              </button>
              <span className="text-xs font-mono font-bold text-[#00F5D4]">ROUTINE MAINTENANCE</span>
            </div>

            <div className="bg-[#0D1422] border border-[#00F5D4]/30 rounded-xl p-3">
              <p className="text-[10px] font-mono text-[#00F5D4]">J.A.R.V.I.S. // SCHEDULE CONTROLLER</p>
              <p className="text-xs text-[#E2F1FF] italic">
                "Pardon the interruption, miss, but optimal system protocols suggest a brief maintenance break before urgency spikes. Timed voiding re-establishes conscious control."
              </p>
            </div>

            {/* COUNTDOWN CLOCK */}
            <div className="bg-[#0D1422] border border-[#00F5D4] rounded-2xl p-6 text-center space-y-3">
              <p className="text-xs font-mono text-[#7A8FA6]">NEXT SCHEDULED MAINTENANCE WINDOW</p>
              <p className="text-4xl font-mono font-black text-white">{formatSecs(secondsUntilVoid)}</p>
              <p className="text-xs font-mono text-[#00F5D4]">Interval: Every {voidIntervalMins} mins</p>
            </div>

            {/* INTERVAL SETTER */}
            <div className="bg-[#0D1422] border border-[#1E314F] rounded-xl p-3.5 space-y-2">
              <p className="text-xs font-mono text-[#7A8FA6]">RETRAINING INTERVAL PRESET</p>
              <div className="grid grid-cols-4 gap-2 text-xs font-mono">
                {[60, 90, 120, 150].map(mins => (
                  <button
                    key={mins}
                    onClick={() => { setVoidIntervalMins(mins); setSecondsUntilVoid(mins * 60); }}
                    className={`p-2 rounded border font-bold ${
                      voidIntervalMins === mins
                        ? 'bg-[#00F5D4] text-black border-[#00F5D4]'
                        : 'bg-[#141F33] text-[#7A8FA6] border-[#1E314F]'
                    }`}
                  >
                    {mins}m
                  </button>
                ))}
              </div>
            </div>

            <button
              onClick={() => {
                setSecondsUntilVoid(voidIntervalMins * 60);
                setVoidLogs(l => [{ id: Date.now(), time: 'Just now', scheduled: true, urge: 1 }, ...l]);
                playHaptic('pulse');
              }}
              className="w-full py-3.5 rounded-xl bg-[#00F5D4] text-black font-mono font-bold text-xs uppercase"
            >
              CONDUCT SCHEDULED MAINTENANCE NOW
            </button>
          </div>
        )}

        {/* ========================================================================= */}
        {/* SCREEN 6: NEURAL RESET (Anxiety Management) */}
        {/* ========================================================================= */}
        {currentScreen === 'NEURAL' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between border-b border-[#1E314F] pb-2">
              <button
                onClick={() => { setIsBreathingActive(false); setCurrentScreen('DASHBOARD'); }}
                className="flex items-center space-x-1 text-xs font-mono text-[#00E5FF]"
              >
                <ArrowLeft className="w-4 h-4" />
                <span>FLIGHT DECK</span>
              </button>
              <span className="text-xs font-mono font-bold text-[#64FFDA]">NEURAL RESET</span>
            </div>

            <div className="bg-[#0D1422] border border-[#64FFDA]/30 rounded-xl p-3">
              <p className="text-[10px] font-mono text-[#64FFDA]">J.A.R.V.I.S. // AUTONOMIC RESET</p>
              <p className="text-xs text-[#E2F1FF] italic">
                "Downshifting neural telemetry, miss. Anxiety provokes the detrusor with fight-or-flight signaling. Tactical respiration quiets the reflex."
              </p>
            </div>

            {/* BOX BREATHING PACER */}
            <div className="bg-[#0D1422] border border-[#64FFDA] rounded-2xl p-6 text-center space-y-4">
              <h3 className="text-xs font-mono font-bold text-[#64FFDA]">4-4-4-4 TACTICAL BOX BREATHING</h3>
              <div className="w-36 h-36 mx-auto rounded-full border-4 border-[#64FFDA] flex flex-col items-center justify-center bg-[#64FFDA]/10">
                <span className="text-xs font-mono font-bold text-[#64FFDA]">{boxBreathPhase}</span>
                <span className="text-4xl font-mono font-black text-white">{boxBreathSecs}s</span>
              </div>
              <button
                onClick={() => setIsBreathingActive(!isBreathingActive)}
                className={`w-full py-3 rounded-lg font-mono font-bold text-xs ${
                  isBreathingActive ? 'bg-red-800 text-white' : 'bg-[#64FFDA] text-black'
                }`}
              >
                {isBreathingActive ? 'PAUSE PACER' : 'ENGAGE BOX BREATHING'}
              </button>
            </div>

            {/* STARK LAB AMBIENT SOUNDSCAPE */}
            <div className="bg-[#0D1422] border border-[#1E314F] rounded-xl p-4 space-y-3">
              <div className="flex justify-between items-center text-xs">
                <span className="font-mono font-bold text-white flex items-center space-x-1">
                  <Volume2 className="w-4 h-4 text-[#FFD700]" />
                  <span>AUDITORY RESONANCE</span>
                </span>
                <button
                  onClick={() => setIsSoundActive(!isSoundActive)}
                  className={`text-[11px] font-mono px-2 py-0.5 rounded ${
                    isSoundActive ? 'bg-[#FFD700] text-black' : 'bg-[#141F33] text-[#7A8FA6]'
                  }`}
                >
                  {isSoundActive ? 'ACTIVE' : 'MUTED'}
                </button>
              </div>

              <div className="space-y-1.5 text-xs font-mono">
                {['ARC_REACTOR_HUM', 'MALIBU_OCEAN', 'BINAURAL_ALPHA'].map(snd => (
                  <button
                    key={snd}
                    onClick={() => setSoundscape(snd)}
                    className={`w-full p-2 rounded text-left border ${
                      soundscape === snd ? 'border-[#FFD700] bg-[#FFD700]/10 text-[#FFD700]' : 'border-[#1E314F] text-[#7A8FA6]'
                    }`}
                  >
                    {snd === 'ARC_REACTOR_HUM' ? '⚡ Arc Reactor Low Hum (432Hz)' :
                     snd === 'MALIBU_OCEAN' ? '🌊 Malibu Coastal Ocean Swell' :
                     '🧠 Binaural Alpha Focus Frequency'}
                  </button>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* ========================================================================= */}
        {/* SCREEN 7: STARK VAULT & DISCRETION */}
        {/* ========================================================================= */}
        {currentScreen === 'VAULT' && (
          <div className="space-y-4">
            <div className="flex items-center justify-between border-b border-[#1E314F] pb-2">
              <button
                onClick={() => setCurrentScreen('DASHBOARD')}
                className="flex items-center space-x-1 text-xs font-mono text-[#00E5FF]"
              >
                <ArrowLeft className="w-4 h-4" />
                <span>FLIGHT DECK</span>
              </button>
              <span className="text-xs font-mono font-bold text-[#FFD700]">STARK VAULT & DISCRETION</span>
            </div>

            <div className="bg-[#0D1422] border border-[#FFD700]/40 rounded-xl p-4 space-y-3">
              <h3 className="text-xs font-mono font-bold text-[#FFD700]">STEALTH DISGUISE TITLE</h3>
              <p className="text-[11px] text-[#7A8FA6]">Select how this application appears in your OS multitasking tray:</p>

              <div className="space-y-2">
                {['Protocol J', 'Stark Home', 'Arc Diagnostics'].map(name => (
                  <button
                    key={name}
                    onClick={() => setStealthName(name)}
                    className={`w-full p-2.5 rounded-lg border text-left flex justify-between items-center text-xs font-mono ${
                      stealthName === name
                        ? 'border-[#FFD700] bg-[#FFD700]/15 text-[#FFD700]'
                        : 'border-[#1E314F] text-[#7A8FA6]'
                    }`}
                  >
                    <span>{name}</span>
                    {stealthName === name && <CheckCircle2 className="w-4 h-4 text-[#FFD700]" />}
                  </button>
                ))}
              </div>
            </div>

            {/* STANDALONE APK DISPATCH SECTION */}
            <div className="bg-[#0D1422] border border-[#00E5FF]/40 rounded-xl p-4 space-y-3">
              <div className="flex items-center space-x-2">
                <Share2 className="w-4 h-4 text-[#00E5FF]" />
                <h3 className="text-xs font-mono font-bold text-[#00E5FF]">STANDALONE APK DISPATCH</h3>
              </div>
              <p className="text-xs text-[#E2F1FF] leading-relaxed">
                Packs the application binary internally and dispatches the standalone .APK package via Android Share Sheet (Bluetooth, Quick Share, Drive, or Direct File Transfer).
              </p>
              <button
                onClick={handleShareApk}
                className="w-full py-3 rounded-lg bg-[#00E5FF] text-black font-mono font-bold text-xs uppercase hover:brightness-110 flex items-center justify-center space-x-2"
              >
                <Download className="w-4 h-4" />
                <span>PACKAGE & DISPATCH STARK APK</span>
              </button>
            </div>

            <div className="bg-[#0D1422] border border-[#00F5D4]/30 rounded-xl p-4 space-y-2">
              <h3 className="text-xs font-mono font-bold text-[#00F5D4] flex items-center space-x-1">
                <Shield className="w-4 h-4" />
                <span>LOCAL AIR-GAPPED PRIVACY</span>
              </h3>
              <p className="text-xs text-[#E2F1FF] leading-relaxed">
                All symptom logs and pelvic telemetry remain 100% on your local device SQLite storage. Zero cloud telemetry. Complete medical dignity.
              </p>
            </div>
          </div>
        )}

        {/* HIGH-TECH STARK APK PACKAGING OVERLAY MODAL */}
        {isSharingApk && (
          <div className="fixed inset-0 bg-black/80 backdrop-blur-sm z-[100] flex items-center justify-center p-4">
            <div className="bg-[#0D1422] border-2 border-[#00E5FF] rounded-2xl p-6 max-w-sm w-full space-y-4 shadow-[0_0_30px_rgba(0,229,255,0.4)]">
              <div className="flex justify-between items-center border-b border-[#1E314F] pb-2">
                <span className="text-[10px] font-mono text-[#00E5FF]">J.A.R.V.I.S. // COMPILER PIPELINE</span>
                <span className="text-[9px] font-mono text-[#7A8FA6]">PORTABLE .APK</span>
              </div>

              <div className="text-center space-y-1">
                <h3 className="text-sm font-mono font-black text-white tracking-wider">PACKAGING STARK APPLICATION</h3>
                <p className="text-[11px] text-[#7A8FA6] font-mono">{stealthName.toUpperCase()}_MARK_L.APK</p>
              </div>

              {/* Progress Steps */}
              <div className="space-y-2 text-xs font-mono">
                <div className={`p-2 rounded flex items-center space-x-2 ${shareStep >= 1 ? 'bg-[#00E5FF]/10 text-[#00E5FF]' : 'text-[#7A8FA6]'}`}>
                  <CheckCircle2 className={`w-3.5 h-3.5 ${shareStep >= 1 ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`} />
                  <span>1. Extracting Mark L runtime binaries...</span>
                </div>
                <div className={`p-2 rounded flex items-center space-x-2 ${shareStep >= 2 ? 'bg-[#00E5FF]/10 text-[#00E5FF]' : 'text-[#7A8FA6]'}`}>
                  <CheckCircle2 className={`w-3.5 h-3.5 ${shareStep >= 2 ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`} />
                  <span>2. Applying 256-bit stealth obfuscation...</span>
                </div>
                <div className={`p-2 rounded flex items-center space-x-2 ${shareStep >= 3 ? 'bg-[#00E5FF]/10 text-[#00E5FF]' : 'text-[#7A8FA6]'}`}>
                  <CheckCircle2 className={`w-3.5 h-3.5 ${shareStep >= 3 ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`} />
                  <span>3. Signing with debug.keystore hash...</span>
                </div>
                <div className={`p-2 rounded flex items-center space-x-2 ${shareStep >= 4 ? 'bg-[#00F5D4]/20 text-[#00F5D4] font-bold' : 'text-[#7A8FA6]'}`}>
                  <CheckCircle2 className={`w-3.5 h-3.5 ${shareStep >= 4 ? 'text-[#00F5D4]' : 'text-[#7A8FA6]'}`} />
                  <span>4. Package Ready (24.8 MB) — Dispatched!</span>
                </div>
              </div>

              {shareSuccessMessage && (
                <p className="text-xs text-[#00F5D4] font-mono text-center bg-[#00F5D4]/10 p-2 rounded">
                  {shareSuccessMessage}
                </p>
              )}

              {shareStep >= 4 && (
                <div className="pt-2 space-y-2">
                  <button
                    onClick={triggerApkDownload}
                    className="w-full py-2.5 rounded bg-[#00E5FF] text-black font-mono font-bold text-xs uppercase flex items-center justify-center space-x-2"
                  >
                    <Download className="w-3.5 h-3.5" />
                    <span>DOWNLOAD APK FILE DIRECTLY</span>
                  </button>
                  <button
                    onClick={() => setIsSharingApk(false)}
                    className="w-full py-2 rounded bg-[#141F33] text-[#7A8FA6] hover:text-white font-mono text-xs"
                  >
                    CLOSE COMPILER HUD
                  </button>
                </div>
              )}
            </div>
          </div>
        )}

        {/* INSTALLATION TROUBLESHOOTING & RED BLOCK BYPASS MODAL */}
        {showInstallHelp && (
          <div className="fixed inset-0 bg-black/85 backdrop-blur-md z-[110] flex items-center justify-center p-4">
            <div className="bg-[#0D1422] border-2 border-[#FFB703] rounded-2xl p-5 max-w-sm w-full space-y-4 shadow-[0_0_35px_rgba(255,183,3,0.3)] max-h-[90vh] overflow-y-auto">
              <div className="flex justify-between items-center border-b border-[#1E314F] pb-2">
                <div className="flex items-center space-x-1.5 text-[#FFB703]">
                  <AlertTriangle className="w-4 h-4" />
                  <span className="text-[11px] font-mono font-bold">STARK BYPASS // INSTALL GUIDE</span>
                </div>
                <button
                  onClick={() => setShowInstallHelp(false)}
                  className="text-xs font-mono text-[#7A8FA6] hover:text-white"
                >
                  ✕
                </button>
              </div>

              <div className="space-y-3 text-xs leading-relaxed">
                {/* EXACT ISSUE: INSTALL VIA USB FADED */}
                <div className="bg-[#141F33] p-3 rounded-xl border border-[#00E5FF]/50 space-y-2">
                  <h4 className="font-mono font-bold text-[#00E5FF] flex items-center space-x-1.5">
                    <span>🔌 WHY IS "INSTALL VIA USB" FADED & BLOCKED?</span>
                  </h4>
                  <p className="text-[#E2F1FF] text-[11px]">
                    The <strong>"Install via USB"</strong> button is disabled because your computer does not currently detect an Android phone connected with <strong>USB Debugging enabled</strong>.
                  </p>
                  <div className="bg-black/50 p-2.5 rounded text-[11px] font-mono text-[#00F5D4] space-y-1">
                    <p className="font-bold text-[#FFB703]">TO MAKE THE USB BUTTON ACTIVE:</p>
                    <p>1. Connect your Android phone to computer using a USB cable.</p>
                    <p>2. On phone: Go to <span className="text-white">Settings → About Phone</span> and tap <span className="text-white">"Build number" 7 times</span> to enable Developer Options.</p>
                    <p>3. Go to <span className="text-white">Settings → System → Developer Options</span> and turn <span className="text-white">USB Debugging</span> to <strong className="text-[#00F5D4]">ON</strong>.</p>
                    <p>4. When prompt appears on phone, tap <strong className="text-white">"Allow USB debugging"</strong>.</p>
                    <p>5. The button will light up!</p>
                  </div>
                </div>

                {/* EASIER OPTION: SCAN QR CODE / NO CABLE NEEDED */}
                <div className="bg-[#141F33] p-3 rounded-xl border border-[#00F5D4]/40 text-center space-y-2">
                  <h4 className="font-mono font-bold text-[#00F5D4] flex items-center justify-center space-x-1">
                    <span>📱 EASIEST WAY: NO USB CABLE NEEDED!</span>
                  </h4>
                  <p className="text-[11px] text-[#E2F1FF]">
                    Scan this QR code with your phone camera to open and install Protocol J directly on your phone:
                  </p>
                  <div className="bg-white p-2.5 rounded-lg inline-block mx-auto">
                    <img
                      src="https://api.qrserver.com/v1/create-qr-code/?size=160x160&data=https://ais-pre-lsdizukwurc2akolwcy63l-382340857324.asia-southeast1.run.app"
                      alt="Install QR Code"
                      className="w-36 h-36"
                    />
                  </div>
                  <p className="text-[10px] font-mono text-[#7A8FA6]">
                    Or open: <a href="https://ais-pre-lsdizukwurc2akolwcy63l-382340857324.asia-southeast1.run.app" target="_blank" rel="noreferrer" className="text-[#00E5FF] underline">ais-pre-lsdizukwurc2akolwcy63l...</a>
                  </p>
                </div>

                {/* ISSUE 2: RED BLOCK / PLAY PROTECT ON APK */}
                <div className="bg-[#141F33] p-3 rounded-xl border border-[#FF5252]/40 space-y-1.5">
                  <h4 className="font-mono font-bold text-[#FF5252] flex items-center space-x-1">
                    <span>🛑 RED SHIELD / PLAY PROTECT POPUP</span>
                  </h4>
                  <p className="text-[#E2F1FF] text-[11px]">
                    If downloading the APK shows "Blocked by Play Protect":
                  </p>
                  <div className="bg-black/40 p-2 rounded text-[11px] font-mono text-[#00F5D4] space-y-1">
                    <p>1. Tap <strong className="text-white">"More details"</strong> (small text on red screen).</p>
                    <p>2. Tap <strong className="text-white">"Install anyway"</strong>.</p>
                  </div>
                </div>
              </div>

              <button
                onClick={() => setShowInstallHelp(false)}
                className="w-full py-2.5 rounded-lg bg-[#FFB703] text-black font-mono font-bold text-xs uppercase"
              >
                GOT IT // RETURN TO SYSTEM
              </button>
            </div>
          </div>
        )}

      </main>

      {/* BOTTOM NAVIGATION BAR */}
      {currentScreen !== 'OVERRIDE' && (
        <nav className="fixed bottom-0 left-0 right-0 max-w-md mx-auto bg-[#0D1422] border-t border-[#1E314F] px-4 py-2 flex justify-around text-[10px] font-mono z-50">
          <button
            onClick={() => setCurrentScreen('DASHBOARD')}
            className={`flex flex-col items-center py-1 ${currentScreen === 'DASHBOARD' ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`}
          >
            <Shield className="w-5 h-5 mb-0.5" />
            <span>Deck</span>
          </button>
          <button
            onClick={() => setCurrentScreen('ARC')}
            className={`flex flex-col items-center py-1 ${currentScreen === 'ARC' ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`}
          >
            <Zap className="w-5 h-5 mb-0.5" />
            <span>Core</span>
          </button>
          <button
            onClick={() => setCurrentScreen('TELEMETRY')}
            className={`flex flex-col items-center py-1 ${currentScreen === 'TELEMETRY' ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`}
          >
            <Activity className="w-5 h-5 mb-0.5" />
            <span>Telemetry</span>
          </button>
          <button
            onClick={() => setCurrentScreen('TIMED')}
            className={`flex flex-col items-center py-1 ${currentScreen === 'TIMED' ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`}
          >
            <Clock className="w-5 h-5 mb-0.5" />
            <span>Timed</span>
          </button>
          <button
            onClick={() => setCurrentScreen('NEURAL')}
            className={`flex flex-col items-center py-1 ${currentScreen === 'NEURAL' ? 'text-[#00E5FF]' : 'text-[#7A8FA6]'}`}
          >
            <Brain className="w-5 h-5 mb-0.5" />
            <span>Neural</span>
          </button>
        </nav>
      )}
    </div>
  );
}
