/*
 *
 *   Copyright (C) 2023 The PixelDust Project
 *   SPDX-License-Identifier: Apache-2.0
 *
 */

package com.pixeldust.launcher;

import android.app.smartspace.SmartspaceTarget;
import android.os.Bundle;

import com.android.launcher3.model.BgDataModel;
import com.android.launcher3.qsb.LauncherUnlockAnimationController;
import com.android.launcher3.uioverrides.QuickstepLauncher;
import com.android.quickstep.SystemUiProxy;

import com.google.android.systemui.smartspace.BcSmartspaceDataProvider;

import com.pixeldust.launcher.PixeldustLauncherModelDelegate.SmartspaceItem;

import java.util.List;
import java.util.stream.Collectors;

public class PixeldustLauncher extends QuickstepLauncher {

    private BcSmartspaceDataProvider mSmartspacePlugin = new BcSmartspaceDataProvider();
    private LauncherUnlockAnimationController mUnlockAnimationController =
            new LauncherUnlockAnimationController(this);

    public BcSmartspaceDataProvider getSmartspacePlugin() {
        return mSmartspacePlugin;
    }

    public LauncherUnlockAnimationController getLauncherUnlockAnimationController() {
        return mUnlockAnimationController;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        SystemUiProxy.INSTANCE.get(this)
                .setLauncherUnlockAnimationController(mUnlockAnimationController);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SystemUiProxy.INSTANCE.get(this).setLauncherUnlockAnimationController(null);
    }

    @Override
    public void onOverlayVisibilityChanged(boolean visible) {
        super.onOverlayVisibilityChanged(visible);
        mUnlockAnimationController.updateSmartspaceState();
    }

    @Override
    public void onPageEndTransition() {
        super.onPageEndTransition();
        mUnlockAnimationController.updateSmartspaceState();
    }

    @Override
    public void bindExtraContainerItems(BgDataModel.FixedContainerItems container) {
        if (container.containerId == -110) {
            List<SmartspaceTarget> targets = container.items.stream()
                    .map(item -> ((SmartspaceItem) item).getSmartspaceTarget())
                    .collect(Collectors.toList());
            mSmartspacePlugin.onTargetsAvailable(targets);
        }
        super.bindExtraContainerItems(container);
    }

}
