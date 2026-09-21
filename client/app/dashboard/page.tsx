"use client";

import { RequireAuth } from "@/components/providers/require-auth";
import { AppShell } from "@/components/layout/app-shell";

const DashboardPage = () => {
  return (
    <RequireAuth>
      <AppShell hideHeader>
      <div>Hello Ji!! Welcome to RepoMind</div>
      </AppShell>
    </RequireAuth>
  )
};

export default DashboardPage;
