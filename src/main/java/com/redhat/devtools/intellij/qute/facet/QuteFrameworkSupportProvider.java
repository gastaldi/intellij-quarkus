/*******************************************************************************
 * Copyright (c) 2019 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution,
 * and is available at https://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package com.redhat.devtools.intellij.qute.facet;

import com.intellij.facet.ui.FacetBasedFrameworkSupportProvider;
import com.intellij.framework.library.DownloadableLibraryService;
import com.intellij.framework.library.FrameworkSupportWithLibrary;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurable;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportConfigurableBase;
import com.intellij.ide.util.frameworkSupport.FrameworkSupportModel;
import com.intellij.ide.util.frameworkSupport.FrameworkVersion;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ui.configuration.libraries.CustomLibraryDescription;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;

public class QuteFrameworkSupportProvider extends FacetBasedFrameworkSupportProvider<QuteFacet> {

    public QuteFrameworkSupportProvider() {
        super(QuteFacet.getQuteFacetType());
    }
    @Override
    protected void setupConfiguration(QuteFacet facet, ModifiableRootModel rootModel, FrameworkVersion version) {

    }

    @NotNull
    @Override
    public FrameworkSupportConfigurable createConfigurable(@NotNull FrameworkSupportModel model) {
        return new QuarkusFrameworkSupportConfigurable(model);
    }

    private class QuarkusFrameworkSupportConfigurable extends FrameworkSupportConfigurableBase implements FrameworkSupportWithLibrary {
        public QuarkusFrameworkSupportConfigurable(FrameworkSupportModel model) {
            super(QuteFrameworkSupportProvider.this, model, QuteFrameworkSupportProvider.this.getVersions(), QuteFrameworkSupportProvider.this.getVersionLabelText());
        }

        public JComponent getComponent() {
            JPanel allPanel = new JPanel(new BorderLayout());
            allPanel.add((Component) ObjectUtils.assertNotNull(super.getComponent()), "Center");
            return allPanel;
        }

        @NotNull
        public CustomLibraryDescription createLibraryDescription() {
            return DownloadableLibraryService.getInstance().createDescriptionForType(QuteLibraryType.class);
        }

        public boolean isLibraryOnly() {
            return false;
        }
    }
}
