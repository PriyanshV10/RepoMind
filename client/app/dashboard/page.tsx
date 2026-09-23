"use client";

import { RequireAuth } from "@/components/providers/require-auth";
import { AppShell } from "@/components/layout/app-shell";
import { RepoDashboard } from "@/components/dashboard/repo-dashboard";

const DashboardPage = () => {
  return (
    <RequireAuth>
      <AppShell hideHeader>
        <RepoDashboard />
      </AppShell>
    </RequireAuth>
  );
};

export default DashboardPage;
