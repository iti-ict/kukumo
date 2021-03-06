/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package iti.kukumo.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.WorkspaceService;

import iti.kukumo.lsp.internal.GherkinWorkspace;

public class KukumoWorkspaceService implements WorkspaceService {

    private final KukumoLanguageServer server;
	private final GherkinWorkspace workspace;

    KukumoWorkspaceService(KukumoLanguageServer server, GherkinWorkspace workspace) {
        this.server = server;
        this.workspace = workspace;
    }

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams params) {
        LoggerUtil.logEntry("workspace.didChangeConfiguration",params);

    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
        LoggerUtil.logEntry("workspace.didChangeWatcherFiles", params);

    }

}