/*******************************************************************************
* Copyright (c) 2020 Red Hat Inc. and others.
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v. 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
* which is available at https://www.apache.org/licenses/LICENSE-2.0.
*
* SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
*
* Contributors:
*     Red Hat Inc. - initial API and implementation
*******************************************************************************/
package com.redhat.devtools.intellij.lsp4mp4ij.psi.core.jaxrs.java;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.redhat.devtools.intellij.MavenModuleImportingTestCase;
import com.redhat.devtools.intellij.lsp4mp4ij.psi.core.PropertiesManagerForJava;
import com.redhat.devtools.intellij.lsp4mp4ij.psi.core.utils.IPsiUtils;
import com.redhat.devtools.intellij.lsp4mp4ij.psi.internal.core.ls.PsiUtilsLSImpl;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4mp.commons.MicroProfileJavaCodeLensParams;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * JAX-RS URL Codelens test for Java file.
 *
 * @author Angelo ZERR
 *
 */
public class JaxRsCodeLensTest extends MavenModuleImportingTestCase {

	@Test
	public void testUrlCodeLensProperties() throws Exception {
		Module javaProject = createMavenModule("hibernate-orm-resteasy", new File("projects/maven/hibernate-orm-resteasy"));
		IPsiUtils utils = PsiUtilsLSImpl.getInstance(myProject);

		MicroProfileJavaCodeLensParams params = new MicroProfileJavaCodeLensParams();
		params.setCheckServerAvailable(false);
		VirtualFile javaFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(ModuleUtilCore.getModuleDirPath(javaProject) + "/src/main/java/org/acme/hibernate/orm/FruitResource.java");
		String uri = VfsUtilCore.virtualToIoFile(javaFile).toURI().toString();

		params.setUri(uri);
		params.setUrlCodeLensEnabled(true);

		// Default port
		assertCodeLenses(8080, params, utils);
	}

	@Test
	public void testUrlCodeLensYaml() throws Exception {
		Module javaProject = createMavenModule("hibernate-orm-resteasy-yaml", new File("projects/maven/hibernate-orm-resteasy-yaml"));
		IPsiUtils utils = PsiUtilsLSImpl.getInstance(myProject);

		MicroProfileJavaCodeLensParams params = new MicroProfileJavaCodeLensParams();
		params.setCheckServerAvailable(false);
		VirtualFile javaFile = LocalFileSystem.getInstance().refreshAndFindFileByPath(ModuleUtilCore.getModuleDirPath(javaProject) + "/src/main/java/org/acme/hibernate/orm/FruitResource.java");
		String uri = VfsUtilCore.virtualToIoFile(javaFile).toURI().toString();
		params.setUri(uri);
		params.setUrlCodeLensEnabled(true);

		// Default port
		assertCodeLenses(8080, params, utils);
	}

	private static void assertCodeLenses(int port, MicroProfileJavaCodeLensParams params, IPsiUtils utils) {
		List<? extends CodeLens> lenses = PropertiesManagerForJava.getInstance().codeLens(params, utils);
		Assert.assertEquals(2, lenses.size());

		// @GET
		// public Fruit[] get() {
		CodeLens lensForGet = lenses.get(0);
		Assert.assertNotNull(lensForGet.getCommand());
		Assert.assertEquals("http://localhost:" + port + "/fruits", lensForGet.getCommand().getTitle());

		// @GET
		// @Path("{id}")
		// public Fruit getSingle(@PathParam Integer id) {
		CodeLens lensForGetSingle = lenses.get(1);
		Assert.assertNotNull(lensForGetSingle.getCommand());
		Assert.assertEquals("http://localhost:" + port + "/fruits/{id}", lensForGetSingle.getCommand().getTitle());
	}

}
